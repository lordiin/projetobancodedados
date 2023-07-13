import { IDisciplina, NewDisciplina } from './disciplina.model';

export const sampleWithRequiredData: IDisciplina = {
  id: 2721,
};

export const sampleWithPartialData: IDisciplina = {
  id: 94003,
  codigo: 'Frozen Plano',
};

export const sampleWithFullData: IDisciplina = {
  id: 64288,
  codigo: 'Tennessine',
  nome: 'East salmon',
};

export const sampleWithNewData: NewDisciplina = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
