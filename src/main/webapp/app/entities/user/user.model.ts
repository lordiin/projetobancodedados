export interface IUser {
  id: number;
  nome: string;
  sobrenome: string;
  matricula?: string;
}

export class User implements IUser {
  constructor(public id: number, public nome: string, public sobrenome: string, public matricula: string) {}
}

export function getUserIdentifier(user: IUser): number {
  return user.id;
}
