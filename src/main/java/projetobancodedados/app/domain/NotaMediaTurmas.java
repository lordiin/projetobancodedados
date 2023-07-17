package projetobancodedados.app.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotaMediaTurmas {

    private String nomeDisciplina;

    private String nomeProfessor;

    private BigDecimal notaMedia;
}
