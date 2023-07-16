export interface IUser {
  id: number;
  matricula?: string;
}

export class User implements IUser {
  constructor(public id: number, public matricula: string) {}
}

export function getUserIdentifier(user: IUser): number {
  return user.id;
}
