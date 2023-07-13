import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TurmaFormService, TurmaFormGroup } from './turma-form.service';
import { ITurma } from '../turma.model';
import { TurmaService } from '../service/turma.service';
import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { DisciplinaService } from 'app/entities/disciplina/service/disciplina.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';

@Component({
  standalone: true,
  selector: 'jhi-turma-update',
  templateUrl: './turma-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TurmaUpdateComponent implements OnInit {
  isSaving = false;
  turma: ITurma | null = null;

  disciplinasSharedCollection: IDisciplina[] = [];
  departamentosSharedCollection: IDepartamento[] = [];

  editForm: TurmaFormGroup = this.turmaFormService.createTurmaFormGroup();

  constructor(
    protected turmaService: TurmaService,
    protected turmaFormService: TurmaFormService,
    protected disciplinaService: DisciplinaService,
    protected departamentoService: DepartamentoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDisciplina = (o1: IDisciplina | null, o2: IDisciplina | null): boolean => this.disciplinaService.compareDisciplina(o1, o2);

  compareDepartamento = (o1: IDepartamento | null, o2: IDepartamento | null): boolean =>
    this.departamentoService.compareDepartamento(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ turma }) => {
      this.turma = turma;
      if (turma) {
        this.updateForm(turma);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const turma = this.turmaFormService.getTurma(this.editForm);
    if (turma.id !== null) {
      this.subscribeToSaveResponse(this.turmaService.update(turma));
    } else {
      this.subscribeToSaveResponse(this.turmaService.create(turma));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITurma>>): void {
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

  protected updateForm(turma: ITurma): void {
    this.turma = turma;
    this.turmaFormService.resetForm(this.editForm, turma);

    this.disciplinasSharedCollection = this.disciplinaService.addDisciplinaToCollectionIfMissing<IDisciplina>(
      this.disciplinasSharedCollection,
      turma.disciplina
    );
    this.departamentosSharedCollection = this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(
      this.departamentosSharedCollection,
      turma.departamento
    );
  }

  protected loadRelationshipsOptions(): void {
    this.disciplinaService
      .query()
      .pipe(map((res: HttpResponse<IDisciplina[]>) => res.body ?? []))
      .pipe(
        map((disciplinas: IDisciplina[]) =>
          this.disciplinaService.addDisciplinaToCollectionIfMissing<IDisciplina>(disciplinas, this.turma?.disciplina)
        )
      )
      .subscribe((disciplinas: IDisciplina[]) => (this.disciplinasSharedCollection = disciplinas));

    this.departamentoService
      .query()
      .pipe(map((res: HttpResponse<IDepartamento[]>) => res.body ?? []))
      .pipe(
        map((departamentos: IDepartamento[]) =>
          this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(departamentos, this.turma?.departamento)
        )
      )
      .subscribe((departamentos: IDepartamento[]) => (this.departamentosSharedCollection = departamentos));
  }
}
