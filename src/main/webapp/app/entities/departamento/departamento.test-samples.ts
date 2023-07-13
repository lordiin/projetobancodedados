import { IDepartamento, NewDepartamento } from './departamento.model';

export const sampleWithRequiredData: IDepartamento = {
  id: 1955,
};

export const sampleWithPartialData: IDepartamento = {
  id: 70829,
  codigo: 'Tin Security',
  nome: 'Usability West Computers',
};

export const sampleWithFullData: IDepartamento = {
  id: 92266,
  codigo: 'Protactinium Bronze',
  nome: 'kookily Operative',
};

export const sampleWithNewData: NewDepartamento = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
