import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AvaliacaoFormService, AvaliacaoFormGroup } from './avaliacao-form.service';
import { IAvaliacao } from '../avaliacao.model';
import { AvaliacaoService } from '../service/avaliacao.service';
import { ITurma } from 'app/entities/turma/turma.model';
import { TurmaService } from 'app/entities/turma/service/turma.service';

@Component({
  standalone: true,
  selector: 'jhi-avaliacao-update',
  templateUrl: './avaliacao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AvaliacaoUpdateComponent implements OnInit {
  isSaving = false;
  avaliacao: IAvaliacao | null = null;

  turmasSharedCollection: ITurma[] = [];

  editForm: AvaliacaoFormGroup = this.avaliacaoFormService.createAvaliacaoFormGroup();

  constructor(
    protected avaliacaoService: AvaliacaoService,
    protected avaliacaoFormService: AvaliacaoFormService,
    protected turmaService: TurmaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTurma = (o1: ITurma | null, o2: ITurma | null): boolean => this.turmaService.compareTurma(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ avaliacao }) => {
      this.avaliacao = avaliacao;
      if (avaliacao) {
        this.updateForm(avaliacao);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const avaliacao = this.avaliacaoFormService.getAvaliacao(this.editForm);
    if (avaliacao.id !== null) {
      this.subscribeToSaveResponse(this.avaliacaoService.update(avaliacao));
    } else {
      // @ts-ignore
      this.subscribeToSaveResponse(this.avaliacaoService.create(avaliacao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAvaliacao>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(avaliacao: IAvaliacao): void {
    this.avaliacao = avaliacao;
    this.avaliacaoFormService.resetForm(this.editForm, avaliacao);

    this.turmasSharedCollection = this.turmaService.addTurmaToCollectionIfMissing<ITurma>(this.turmasSharedCollection, avaliacao.turma);
  }

  protected loadRelationshipsOptions(): void {
    this.turmaService
      .query()
      .pipe(map((res: HttpResponse<ITurma[]>) => res.body ?? []))
      .pipe(map((turmas: ITurma[]) => this.turmaService.addTurmaToCollectionIfMissing<ITurma>(turmas, this.avaliacao?.turma)))
      .subscribe((turmas: ITurma[]) => (this.turmasSharedCollection = turmas));
  }
}
