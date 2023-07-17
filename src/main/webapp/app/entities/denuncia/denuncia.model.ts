import { IAvaliacao } from 'app/entities/avaliacao/avaliacao.model';

export interface IDenuncia {
  id: number;
  motivo?: string | null;
  avaliacao?: IAvaliacao | null;
}

export type NewDenuncia = Omit<IDenuncia, 'id'> & { id: null };
