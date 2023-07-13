import { ITurma } from 'app/entities/turma/turma.model';
import { IUser } from '../user/user.model';

export interface IAvaliacao {
  id: number;
  descricao?: string | null;
  nota?: number | null;
  turma?: Pick<ITurma, 'id'> | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewAvaliacao = Omit<IAvaliacao, 'id'> & { id: null };
