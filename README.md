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

## Procedure e View

### Construção da Procedure

```
CREATE OR REPLACE FUNCTION obter_nota_media_turma()
  RETURNS TABLE (turma_id BIGINT, turma VARCHAR(255), nota_media NUMERIC)
AS $$
            BEGIN
            RETURN QUERY
            SELECT d.nome as nomeDisciplina, p.nome as nomeProfessor, AVG(a.nota) AS notaMedia
            FROM turma t
                     JOIN avaliacao a ON t.id = a.turma_id
                     JOIN professor p on t.professor_id = p.id
                     JOIN disciplina d on t.disciplina_id = d.id
            GROUP BY d.nome, p.nome;
            END;
$$
            LANGUAGE plpgsql;
```

### Construção da View

```
CREATE
            OR REPLACE VIEW avaliacoes_por_usuario AS
            SELECT u.id AS user_id,
                   u.matricula,
                   u.nome,
                   u.sobrenome,
                   a.id AS avaliacao_id,
                   a.descricao,
                   a.nota
            FROM usuario u
                     JOIN avaliacao a ON u.id = a.user_id
            order by a.id desc;
```
