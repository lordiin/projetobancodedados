import { IAvaliacao, NewAvaliacao } from './avaliacao.model';

export const sampleWithRequiredData: IAvaliacao = {
  id: 69245,
};

export const sampleWithPartialData: IAvaliacao = {
  id: 15941,
  nota: 94572,
};

export const sampleWithFullData: IAvaliacao = {
  id: 59412,
  descricao: 'back',
  nota: 97473,
};

export const sampleWithNewData: NewAvaliacao = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
