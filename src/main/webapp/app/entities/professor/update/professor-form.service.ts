import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProfessor, NewProfessor } from '../professor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProfessor for edit and NewProfessorFormGroupInput for create.
 */
type ProfessorFormGroupInput = IProfessor | PartialWithRequiredKeyOf<NewProfessor>;

type ProfessorFormDefaults = Pick<NewProfessor, 'id'>;

type ProfessorFormGroupContent = {
  id: FormControl<IProfessor['id'] | NewProfessor['id']>;
  nome: FormControl<IProfessor['nome']>;
  email: FormControl<IProfessor['email']>;
  departamento: FormControl<IProfessor['departamento']>;
};

export type ProfessorFormGroup = FormGroup<ProfessorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProfessorFormService {
  createProfessorFormGroup(professor: ProfessorFormGroupInput = { id: null }): ProfessorFormGroup {
    const professorRawValue = {
      ...this.getFormDefaults(),
      ...professor,
    };
    return new FormGroup<ProfessorFormGroupContent>({
      id: new FormControl(
        { value: professorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nome: new FormControl(professorRawValue.nome),
      email: new FormControl(professorRawValue.email),
      departamento: new FormControl(professorRawValue.departamento),
    });
  }

  getProfessor(form: ProfessorFormGroup): IProfessor | NewProfessor {
    return form.getRawValue() as IProfessor | NewProfessor;
  }

  resetForm(form: ProfessorFormGroup, professor: ProfessorFormGroupInput): void {
    const professorRawValue = { ...this.getFormDefaults(), ...professor };
    form.reset(
      {
        ...professorRawValue,
        id: { value: professorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProfessorFormDefaults {
    return {
      id: null,
    };
  }
}
