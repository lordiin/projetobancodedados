package projetobancodedados.app.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Professor.
 */
@Entity
@Table(name = "professor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Professor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    private Departamento departamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Professor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Professor nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Professor departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Professor)) {
            return false;
        }
        return id != null && id.equals(((Professor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Professor{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
