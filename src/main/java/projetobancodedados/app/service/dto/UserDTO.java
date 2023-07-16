package projetobancodedados.app.service.dto;

import java.io.Serializable;
import lombok.*;
import projetobancodedados.app.domain.User;

/**
 * A DTO representing a user, with only the public attributes.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String matricula;

    private byte[] imagem;

    private String imagemContentType;

    public UserDTO(User user) {
        this.id = user.getId();
        this.matricula = user.getMatricula();
        this.imagem = user.getImagem();
        this.imagemContentType = user.getImagemContentType();
    }
}
