export interface IUser {
  id: number | null;
  matricula?: string;
  nome?: string | null;
  sobrenome?: string | null;
  email?: string;
  activated?: boolean;
  authorities?: string[];
}

export class User implements IUser {
  constructor(
    public id: number | null,
    public matricula?: string,
    public nome?: string | null,
    public sobrenome?: string | null,
    public email?: string,
    public activated?: boolean,
    public authorities?: string[]
  ) {}
}
