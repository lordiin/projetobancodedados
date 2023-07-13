import { IProfessor, NewProfessor } from './professor.model';

export const sampleWithRequiredData: IProfessor = {
  id: 12377,
};

export const sampleWithPartialData: IProfessor = {
  id: 47538,
};

export const sampleWithFullData: IProfessor = {
  id: 32791,
  nome: 'Chilean',
  email: 'Drake.Padberg@hotmail.com',
};

export const sampleWithNewData: NewProfessor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
