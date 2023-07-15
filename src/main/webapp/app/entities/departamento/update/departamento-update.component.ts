import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { DepartamentoFormService, DepartamentoFormGroup } from './departamento-form.service';
import { IDepartamento } from '../departamento.model';
import { DepartamentoService } from '../service/departamento.service';

@Component({
  standalone: true,
  selector: 'jhi-departamento-update',
  templateUrl: './departamento-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DepartamentoUpdateComponent implements OnInit {
  isSaving = false;
  departamento: IDepartamento | null = null;

  editForm: DepartamentoFormGroup = this.departamentoFormService.createDepartamentoFormGroup();

  constructor(
    protected departamentoService: DepartamentoService,
    protected departamentoFormService: DepartamentoFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departamento }) => {
      this.departamento = departamento;
      if (departamento) {
        this.updateForm(departamento);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const departamento = this.departamentoFormService.getDepartamento(this.editForm);
    if (departamento.id !== null) {
      this.subscribeToSaveResponse(this.departamentoService.update(departamento));
    } else {
      // @ts-ignore
      this.subscribeToSaveResponse(this.departamentoService.create(departamento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartamento>>): void {
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

  protected updateForm(departamento: IDepartamento): void {
    this.departamento = departamento;
    this.departamentoFormService.resetForm(this.editForm, departamento);
  }
}
