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
import { DenunciaService } from '../../denuncia/service/denuncia.service';
import { IDenuncia, NewDenuncia } from '../../denuncia/denuncia.model';

@Component({
  standalone: true,
  selector: 'jhi-avaliacao-denuncia',
  templateUrl: './avaliacao-denuncia.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AvaliacaoDenunciaComponent implements OnInit {
  isSaving = false;
  motivo: string = '';
  avaliacaoId: number;
  denuncia: NewDenuncia;
  turmaId: number;
  user: IUser;
  routeSub: any;

  constructor(
    protected avaliacaoService: AvaliacaoService,
    protected turmaService: TurmaService,
    protected activatedRoute: ActivatedRoute,
    protected denunciaService: DenunciaService
  ) {}

  compareTurma = (o1: ITurma | null, o2: ITurma | null): boolean => this.turmaService.compareTurma(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      if (params['avaliacaoId']) {
        this.avaliacaoId = params['avaliacaoId'];
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    // this.isSaving = true;
    const objeto = {
      motivo: this.motivo,
    };
    this.denuncia = objeto as NewDenuncia;
    this.denunciaService.createDenuncia(this.denuncia, this.avaliacaoId).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
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
}
