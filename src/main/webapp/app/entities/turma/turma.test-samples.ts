import { ITurma, NewTurma } from './turma.model';

export const sampleWithRequiredData: ITurma = {
  id: 96910,
};

export const sampleWithPartialData: ITurma = {
  id: 37692,
  periodo: 'metrics architectures customized',
  professor: 'white Chicken withdrawal',
  local: 'Catonsville worth',
};

export const sampleWithFullData: ITurma = {
  id: 48551,
  turma: 'Tuna Avon Product',
  periodo: 'dish Designer deliver',
  professor: 'Account New Plastic',
  horario: 'maroon rich terrorize',
  vagasOcupadas: 'volt',
  totalVagas: 'Division id black',
  local: 'interface',
};

export const sampleWithNewData: NewTurma = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
