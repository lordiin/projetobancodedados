import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { DisciplinaFormService, DisciplinaFormGroup } from './disciplina-form.service';
import { IDisciplina } from '../disciplina.model';
import { DisciplinaService } from '../service/disciplina.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';

@Component({
  standalone: true,
  selector: 'jhi-disciplina-update',
  templateUrl: './disciplina-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DisciplinaUpdateComponent implements OnInit {
  isSaving = false;
  disciplina: IDisciplina | null = null;

  departamentosSharedCollection: IDepartamento[] = [];

  editForm: DisciplinaFormGroup = this.disciplinaFormService.createDisciplinaFormGroup();

  constructor(
    protected disciplinaService: DisciplinaService,
    protected disciplinaFormService: DisciplinaFormService,
    protected departamentoService: DepartamentoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDepartamento = (o1: IDepartamento | null, o2: IDepartamento | null): boolean =>
    this.departamentoService.compareDepartamento(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disciplina }) => {
      this.disciplina = disciplina;
      if (disciplina) {
        this.updateForm(disciplina);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const disciplina = this.disciplinaFormService.getDisciplina(this.editForm);
    if (disciplina.id !== null) {
      this.subscribeToSaveResponse(this.disciplinaService.update(disciplina));
    } else {
      this.subscribeToSaveResponse(this.disciplinaService.create(disciplina));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDisciplina>>): void {
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

  protected updateForm(disciplina: IDisciplina): void {
    this.disciplina = disciplina;
    this.disciplinaFormService.resetForm(this.editForm, disciplina);

    this.departamentosSharedCollection = this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(
      this.departamentosSharedCollection,
      disciplina.departamento
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departamentoService
      .query()
      .pipe(map((res: HttpResponse<IDepartamento[]>) => res.body ?? []))
      .pipe(
        map((departamentos: IDepartamento[]) =>
          this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(departamentos, this.disciplina?.departamento)
        )
      )
      .subscribe((departamentos: IDepartamento[]) => (this.departamentosSharedCollection = departamentos));
  }
}
