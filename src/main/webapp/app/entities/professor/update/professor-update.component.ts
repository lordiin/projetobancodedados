import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ProfessorFormService, ProfessorFormGroup } from './professor-form.service';
import { IProfessor } from '../professor.model';
import { ProfessorService } from '../service/professor.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';

@Component({
  standalone: true,
  selector: 'jhi-professor-update',
  templateUrl: './professor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProfessorUpdateComponent implements OnInit {
  isSaving = false;
  professor: IProfessor | null = null;

  departamentosSharedCollection: IDepartamento[] = [];

  editForm: ProfessorFormGroup = this.professorFormService.createProfessorFormGroup();

  constructor(
    protected professorService: ProfessorService,
    protected professorFormService: ProfessorFormService,
    protected departamentoService: DepartamentoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDepartamento = (o1: IDepartamento | null, o2: IDepartamento | null): boolean =>
    this.departamentoService.compareDepartamento(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ professor }) => {
      this.professor = professor;
      if (professor) {
        this.updateForm(professor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const professor = this.professorFormService.getProfessor(this.editForm);
    if (professor.id !== null) {
      this.subscribeToSaveResponse(this.professorService.update(professor));
    } else {
      // @ts-ignore
      this.subscribeToSaveResponse(this.professorService.create(professor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfessor>>): void {
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

  protected updateForm(professor: IProfessor): void {
    this.professor = professor;
    this.professorFormService.resetForm(this.editForm, professor);

    this.departamentosSharedCollection = this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(
      this.departamentosSharedCollection,
      professor.departamento
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departamentoService
      .query()
      .pipe(map((res: HttpResponse<IDepartamento[]>) => res.body ?? []))
      .pipe(
        map((departamentos: IDepartamento[]) =>
          this.departamentoService.addDepartamentoToCollectionIfMissing<IDepartamento>(departamentos, this.professor?.departamento)
        )
      )
      .subscribe((departamentos: IDepartamento[]) => (this.departamentosSharedCollection = departamentos));
  }
}
