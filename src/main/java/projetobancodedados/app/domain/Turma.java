package projetobancodedados.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Turma.
 */
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

    @Column(name = "professor")
    private String professor;

    @Column(name = "horario")
    private String horario;

    @Column(name = "vagas_ocupadas")
    private String vagasOcupadas;

    @Column(name = "total_vagas")
    private String totalVagas;

    @Column(name = "local")
    private String local;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "departamento" }, allowSetters = true)
    private Disciplina disciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    private Departamento departamento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Turma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTurma() {
        return this.turma;
    }

    public Turma turma(String turma) {
        this.setTurma(turma);
        return this;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getPeriodo() {
        return this.periodo;
    }

    public Turma periodo(String periodo) {
        this.setPeriodo(periodo);
        return this;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getProfessor() {
        return this.professor;
    }

    public Turma professor(String professor) {
        this.setProfessor(professor);
        return this;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getHorario() {
        return this.horario;
    }

    public Turma horario(String horario) {
        this.setHorario(horario);
        return this;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getVagasOcupadas() {
        return this.vagasOcupadas;
    }

    public Turma vagasOcupadas(String vagasOcupadas) {
        this.setVagasOcupadas(vagasOcupadas);
        return this;
    }

    public void setVagasOcupadas(String vagasOcupadas) {
        this.vagasOcupadas = vagasOcupadas;
    }

    public String getTotalVagas() {
        return this.totalVagas;
    }

    public Turma totalVagas(String totalVagas) {
        this.setTotalVagas(totalVagas);
        return this;
    }

    public void setTotalVagas(String totalVagas) {
        this.totalVagas = totalVagas;
    }

    public String getLocal() {
        return this.local;
    }

    public Turma local(String local) {
        this.setLocal(local);
        return this;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Disciplina getDisciplina() {
        return this.disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Turma disciplina(Disciplina disciplina) {
        this.setDisciplina(disciplina);
        return this;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Turma departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        return "Turma{" +
            "id=" + getId() +
            ", turma='" + getTurma() + "'" +
            ", periodo='" + getPeriodo() + "'" +
            ", professor='" + getProfessor() + "'" +
            ", horario='" + getHorario() + "'" +
            ", vagasOcupadas='" + getVagasOcupadas() + "'" +
            ", totalVagas='" + getTotalVagas() + "'" +
            ", local='" + getLocal() + "'" +
            "}";
    }
}
