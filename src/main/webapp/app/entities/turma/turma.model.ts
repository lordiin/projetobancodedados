import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { IProfessor } from '../professor/professor.model';

export interface ITurma {
  id: number;
  turma?: string | null;
  periodo?: string | null;
  horario?: string | null;
  vagasOcupadas?: string | null;
  totalVagas?: string | null;
  local?: string | null;
  disciplina?: IDisciplina | null;
  departamento?: IDepartamento | null;
  professor?: IProfessor | null;
}

export type NewTurma = Omit<ITurma, 'id'> & { id: null };
