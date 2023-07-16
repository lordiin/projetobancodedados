import { IDepartamento } from 'app/entities/departamento/departamento.model';

export interface IProfessor {
  id: number;
  nome?: string | null;
  departamento?: IDepartamento | null;
}

export type NewProfessor = Omit<IProfessor, 'id'> & { id: null };
