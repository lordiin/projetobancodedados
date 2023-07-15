export class Account {
  constructor(
    public activated: boolean,
    public authorities: string[],
    public email: string,
    public nome: string | null,
    public langKey: string,
    public sobrenome: string | null,
    public login: string,
    public imagem: any,
    public imagemContentType: string
  ) {}
}
