package projetobancodedados.app.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import projetobancodedados.app.config.Constants;
import projetobancodedados.app.domain.Authority;
import projetobancodedados.app.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */

@Getter
@Setter
public class AdminUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String matricula;

    @Size(max = 50)
    private String nome;

    @Size(max = 50)
    private String sobrenome;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private byte[] imagem;

    private String imagemContentType;

    private boolean activated = false;

    private Set<String> authorities;

    public AdminUserDTO() {
        // Empty constructor needed for Jackson.
    }

    public AdminUserDTO(User user) {
        this.id = user.getId();
        this.matricula = user.getMatricula();
        this.imagem = user.getImagem();
        this.imagemContentType = user.getImagemContentType();
        this.nome = user.getNome();
        this.sobrenome = user.getSobrenome();
        this.email = user.getEmail();
        this.activated = user.isActivated();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminUserDTO{" +
            "login='" + matricula + '\'' +
            ", nome='" + nome + '\'' +
            ", sobrenome='" + sobrenome + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", authorities=" + authorities +
            "}";
    }
}
