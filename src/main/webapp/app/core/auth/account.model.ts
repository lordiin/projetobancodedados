export class Account {
  constructor(
    public activated: boolean,
    public authorities: string[],
    public email: string,
    public nome: string | null,
    public sobrenome: string | null,
    public matricula: string,
    public imagem: any,
    public imagemContentType: string
  ) {}
}
