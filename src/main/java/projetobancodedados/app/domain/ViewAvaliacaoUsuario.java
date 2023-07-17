package projetobancodedados.app.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewAvaliacaoUsuario {

    private Integer userId;
    private String userMatricula;
    private String userNome;
    private String userSobrenome;
    private Integer avaliacaoId;
    private String avaliacaoDescricao;
    private Integer avaliacaoNota;
}
