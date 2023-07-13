export interface IDepartamento {
  id: number;
  codigo?: string | null;
  nome?: string | null;
}

export type NewDepartamento = Omit<IDepartamento, 'id'> & { id: null };
