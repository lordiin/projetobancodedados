import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { DenunciaFormService, DenunciaFormGroup } from './denuncia-form.service';
import { IDenuncia } from '../denuncia.model';
import { DenunciaService } from '../service/denuncia.service';
import { IAvaliacao } from 'app/entities/avaliacao/avaliacao.model';
import { AvaliacaoService } from 'app/entities/avaliacao/service/avaliacao.service';

@Component({
  standalone: true,
  selector: 'jhi-denuncia-update',
  templateUrl: './denuncia-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DenunciaUpdateComponent implements OnInit {
  isSaving = false;
  denuncia: IDenuncia | null = null;

  avaliacaosSharedCollection: IAvaliacao[] = [];

  editForm: DenunciaFormGroup = this.denunciaFormService.createDenunciaFormGroup();

  constructor(
    protected denunciaService: DenunciaService,
    protected denunciaFormService: DenunciaFormService,
    protected avaliacaoService: AvaliacaoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAvaliacao = (o1: IAvaliacao | null, o2: IAvaliacao | null): boolean => this.avaliacaoService.compareAvaliacao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ denuncia }) => {
      this.denuncia = denuncia;
      if (denuncia) {
        this.updateForm(denuncia);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const denuncia = this.denunciaFormService.getDenuncia(this.editForm);
    if (denuncia.id !== null) {
      this.subscribeToSaveResponse(this.denunciaService.update(denuncia));
    } else {
      this.subscribeToSaveResponse(this.denunciaService.create(denuncia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDenuncia>>): void {
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

  protected updateForm(denuncia: IDenuncia): void {
    this.denuncia = denuncia;
    this.denunciaFormService.resetForm(this.editForm, denuncia);

    this.avaliacaosSharedCollection = this.avaliacaoService.addAvaliacaoToCollectionIfMissing<IAvaliacao>(
      this.avaliacaosSharedCollection,
      denuncia.avaliacao
    );
  }

  protected loadRelationshipsOptions(): void {
    this.avaliacaoService
      .query()
      .pipe(map((res: HttpResponse<IAvaliacao[]>) => res.body ?? []))
      .pipe(
        map((avaliacaos: IAvaliacao[]) =>
          this.avaliacaoService.addAvaliacaoToCollectionIfMissing<IAvaliacao>(avaliacaos, this.denuncia?.avaliacao)
        )
      )
      .subscribe((avaliacaos: IAvaliacao[]) => (this.avaliacaosSharedCollection = avaliacaos));
  }
}
