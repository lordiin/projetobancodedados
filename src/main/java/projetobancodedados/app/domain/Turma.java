package projetobancodedados.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A Turma.
 */
@Getter
@Setter
@Entity
@Table(name = "turma")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Turma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "turma")
    private String turma;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "horario")
    private String horario;

    @Column(name = "vagas_ocupadas")
    private String vagasOcupadas;

    @Column(name = "total_vagas")
    private String totalVagas;

    @Column(name = "local")
    private String local;

    @JoinColumn(name = "professor_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "departamento" }, allowSetters = true)
    private Professor professor;

    @JoinColumn(name = "disciplina_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "departamento" }, allowSetters = true)
    private Disciplina disciplina;

    @JoinColumn(name = "departamento_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Departamento departamento;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Turma)) {
            return false;
        }
        return id != null && id.equals(((Turma) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Avaliacao{" + "professor=" + professor + ", disciplina=" + disciplina + ", departamento=" + departamento + '}';
    }
}
