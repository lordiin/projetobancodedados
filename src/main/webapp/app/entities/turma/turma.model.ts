import { IDisciplina } from 'app/entities/disciplina/disciplina.model';
import { IDepartamento } from 'app/entities/departamento/departamento.model';

export interface ITurma {
  id: number;
  turma?: string | null;
  periodo?: string | null;
  professor?: string | null;
  horario?: string | null;
  vagasOcupadas?: string | null;
  totalVagas?: string | null;
  local?: string | null;
  disciplina?: Pick<IDisciplina, 'id'> | null;
  departamento?: Pick<IDepartamento, 'id'> | null;
}

export type NewTurma = Omit<ITurma, 'id'> & { id: null };
