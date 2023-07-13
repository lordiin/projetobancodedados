import { IDenuncia, NewDenuncia } from './denuncia.model';

export const sampleWithRequiredData: IDenuncia = {
  id: 8329,
};

export const sampleWithPartialData: IDenuncia = {
  id: 17183,
  motivo: 'visionary Leu magenta',
};

export const sampleWithFullData: IDenuncia = {
  id: 89604,
  motivo: 'Chief Iran virtual',
};

export const sampleWithNewData: NewDenuncia = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
