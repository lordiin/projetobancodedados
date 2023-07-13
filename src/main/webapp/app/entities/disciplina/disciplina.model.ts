import { IDepartamento } from 'app/entities/departamento/departamento.model';

export interface IDisciplina {
  id: number;
  codigo?: string | null;
  nome?: string | null;
  departamento?: Pick<IDepartamento, 'id'> | null;
}

export type NewDisciplina = Omit<IDisciplina, 'id'> & { id: null };
