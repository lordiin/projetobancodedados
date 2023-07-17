import { Component, OnInit, Input } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAvaliacao, NewAvaliacao } from '../avaliacao.model';
import { AvaliacaoService } from '../service/avaliacao.service';
import { ITurma } from 'app/entities/turma/turma.model';
import { TurmaService } from 'app/entities/turma/service/turma.service';
import { IUser } from '../../user/user.model';

@Component({
  standalone: true,
  selector: 'jhi-avaliacao-create',
  templateUrl: './avaliacao-create.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AvaliacaoCreateComponent implements OnInit {
  isSaving = false;
  descricao: string = '';
  nota: number;
  avaliacao: NewAvaliacao;
  turmaId: number;
  user: IUser;
  routeSub: any;

  constructor(
    protected avaliacaoService: AvaliacaoService,
    protected turmaService: TurmaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTurma = (o1: ITurma | null, o2: ITurma | null): boolean => this.turmaService.compareTurma(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      if (params['turmaId']) {
        this.turmaId = params['turmaId'];
      }
    });
    console.log(this.turmaId);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    // this.isSaving = true;
    const objeto = {
      descricao: this.descricao,
      nota: this.nota,
    };
    this.avaliacao = objeto as NewAvaliacao;
    this.avaliacaoService.createAvaliacao(this.avaliacao, this.turmaId).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
    // this.subscribeToSaveResponse(this.avaliacaoService.create(this.avaliacao));
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
}
