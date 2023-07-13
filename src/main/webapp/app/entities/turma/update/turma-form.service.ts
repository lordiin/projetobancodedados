import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITurma, NewTurma } from '../turma.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITurma for edit and NewTurmaFormGroupInput for create.
 */
type TurmaFormGroupInput = ITurma | PartialWithRequiredKeyOf<NewTurma>;

type TurmaFormDefaults = Pick<NewTurma, 'id'>;

type TurmaFormGroupContent = {
  id: FormControl<ITurma['id'] | NewTurma['id']>;
  turma: FormControl<ITurma['turma']>;
  periodo: FormControl<ITurma['periodo']>;
  professor: FormControl<ITurma['professor']>;
  horario: FormControl<ITurma['horario']>;
  vagasOcupadas: FormControl<ITurma['vagasOcupadas']>;
  totalVagas: FormControl<ITurma['totalVagas']>;
  local: FormControl<ITurma['local']>;
  disciplina: FormControl<ITurma['disciplina']>;
  departamento: FormControl<ITurma['departamento']>;
};

export type TurmaFormGroup = FormGroup<TurmaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TurmaFormService {
  createTurmaFormGroup(turma: TurmaFormGroupInput = { id: null }): TurmaFormGroup {
    const turmaRawValue = {
      ...this.getFormDefaults(),
      ...turma,
    };
    return new FormGroup<TurmaFormGroupContent>({
      id: new FormControl(
        { value: turmaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      turma: new FormControl(turmaRawValue.turma),
      periodo: new FormControl(turmaRawValue.periodo),
      professor: new FormControl(turmaRawValue.professor),
      horario: new FormControl(turmaRawValue.horario),
      vagasOcupadas: new FormControl(turmaRawValue.vagasOcupadas),
      totalVagas: new FormControl(turmaRawValue.totalVagas),
      local: new FormControl(turmaRawValue.local),
      disciplina: new FormControl(turmaRawValue.disciplina),
      departamento: new FormControl(turmaRawValue.departamento),
    });
  }

  getTurma(form: TurmaFormGroup): ITurma | NewTurma {
    return form.getRawValue() as ITurma | NewTurma;
  }

  resetForm(form: TurmaFormGroup, turma: TurmaFormGroupInput): void {
    const turmaRawValue = { ...this.getFormDefaults(), ...turma };
    form.reset(
      {
        ...turmaRawValue,
        id: { value: turmaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TurmaFormDefaults {
    return {
      id: null,
    };
  }
}
