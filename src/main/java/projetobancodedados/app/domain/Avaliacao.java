package projetobancodedados.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

/**
 * A Avaliacao.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "avaliacao")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Avaliacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "nota")
    private Integer nota;

    //    @ManyToOne(fetch = FetchType.LAZY)
    ////  @JsonIgnoreProperties(value = { "turmas" }, allowSetters = true)
    //    private Turma turma;

    @JoinColumn(name = "turma_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    //    @JsonIgnoreProperties(value = { "disciplina", "departamento" }, allowSetters = true)
    private Turma turma;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Override
    public String toString() {
        return "Avaliacao{" + "turma=" + turma + ", user=" + user + '}';
    }
}
