package projetobancodedados.app.repository;

import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import projetobancodedados.app.domain.User;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from user", nativeQuery = true)
    Page<User> findAll(Pageable pageable);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByMatricula(String matricula);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByMatricula(String matricula);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    @Query(
        value = "INSERT INTO usuario (matricula, imagem, imagem_content_type, password_hash, nome, sobrenome, email, activated, authorities) " +
        "VALUES (:matricula, :imagem, :imagemContentType, :password, :nome, :sobrenome, :email, :activated)",
        nativeQuery = true
    )
    void save(
        @Param("matricula") String matricula,
        @Param("imagem") byte[] imagem,
        @Param("imagemContentType") String imagemContentType,
        @Param("password") String password,
        @Param("nome") String nome,
        @Param("sobrenome") String sobrenome,
        @Param("email") String email,
        @Param("activated") Boolean activated
    );

    @Query(
        value = "UPDATE usuario set matricula = :matricula, imagem = :imagem, imagem_content_type = :imagemContentType, password_hash = :password," +
        " nome = :nome, sobrenome = :sobrenome, email = :email, activated = :activated where id = :id",
        nativeQuery = true
    )
    void save(
        @Param("id") Long id,
        @Param("matricula") String matricula,
        @Param("imagem") byte[] imagem,
        @Param("imagemContentType") String imagemContentType,
        @Param("password") String password,
        @Param("nome") String nome,
        @Param("sobrenome") String sobrenome,
        @Param("email") String email,
        @Param("activated") Boolean activated
    );

    @Query(value = "select u.id from usuario u where u.matricula = :matricula", nativeQuery = true)
    Long findUsuarioByMatricula(String matricula);
}
