import { IDepartamento } from 'app/entities/departamento/departamento.model';

export interface IProfessor {
  id: number;
  nome?: string | null;
  email?: string | null;
  departamento?: Pick<IDepartamento, 'id'> | null;
}

export type NewProfessor = Omit<IProfessor, 'id'> & { id: null };
