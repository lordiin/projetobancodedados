# Projeto Banco de Dados

- Projeto de banco de dados desenvolvido por Levi Braz - 180076744

## Dependências do projeto:

- Projeto desenvolvido em Angular (frontend) + Java (backend) + PostgreSQL (database)

### Para rodar o projeto será necessário:

- Instalar Node versão 18.16
- Instalar Java SDK 17
- Instalar PostgreSQL 14

## Ao abrir o projeto:

- Antes de buildar o projeto, é necessário instalar as dependências do node rodando o comando:

```
npm install
```

- Para conexão com o banco, o projeto está configurando com o usuário e senha padrão do postgreSQL.
  Caso o seu não seja o padrão, será necessário alterar o arquivo `application-dev.yml` no seguinte trecho:

```
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://localhost:5432/projetobd
    username: {{seu_usuario}}
    password: {{sua_senha}}
```

- Caso queira criar um banco com um nome diferente, basta trocar na `url`:

```
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://localhost:5432/{{nome_do_banco}}
```

## Ao buildar o projeto:

- As tabelas serão criadas por meio de queries e populadas com o framework Liquibase por meio de arquivos csv.
- Os changelogs que possuem as queries estão no diretório:
  `src/main/resources/config/liquibase/changelog`
- Para logar na conta que é admin:
  `matricula: admin` `senha: admin`
