import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAvaliacao, NewAvaliacao } from '../avaliacao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAvaliacao for edit and NewAvaliacaoFormGroupInput for create.
 */
type AvaliacaoFormGroupInput = IAvaliacao | PartialWithRequiredKeyOf<NewAvaliacao>;

type AvaliacaoFormDefaults = Pick<NewAvaliacao, 'id'>;

type AvaliacaoFormGroupContent = {
  id: FormControl<IAvaliacao['id'] | NewAvaliacao['id']>;
  descricao: FormControl<IAvaliacao['descricao']>;
  nota: FormControl<IAvaliacao['nota']>;
  turma: FormControl<IAvaliacao['turma']>;
};

export type AvaliacaoFormGroup = FormGroup<AvaliacaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AvaliacaoFormService {
  createAvaliacaoFormGroup(avaliacao: AvaliacaoFormGroupInput = { id: null }): AvaliacaoFormGroup {
    const avaliacaoRawValue = {
      ...this.getFormDefaults(),
      ...avaliacao,
    };
    return new FormGroup<AvaliacaoFormGroupContent>({
      id: new FormControl(
        { value: avaliacaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      descricao: new FormControl(avaliacaoRawValue.descricao),
      nota: new FormControl(avaliacaoRawValue.nota),
      turma: new FormControl(avaliacaoRawValue.turma),
    });
  }

  getAvaliacao(form: AvaliacaoFormGroup): IAvaliacao | NewAvaliacao {
    return form.getRawValue() as IAvaliacao | NewAvaliacao;
  }

  resetForm(form: AvaliacaoFormGroup, avaliacao: AvaliacaoFormGroupInput): void {
    const avaliacaoRawValue = { ...this.getFormDefaults(), ...avaliacao };
    form.reset(
      {
        ...avaliacaoRawValue,
        id: { value: avaliacaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AvaliacaoFormDefaults {
    return {
      id: null,
    };
  }
}
