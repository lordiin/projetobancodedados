package projetobancodedados.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import projetobancodedados.app.web.rest.TestUtil;

class DisciplinaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disciplina.class);
        Disciplina disciplina1 = new Disciplina();
        disciplina1.setId(1L);
        Disciplina disciplina2 = new Disciplina();
        disciplina2.setId(disciplina1.getId());
        assertThat(disciplina1).isEqualTo(disciplina2);
        disciplina2.setId(2L);
        assertThat(disciplina1).isNotEqualTo(disciplina2);
        disciplina1.setId(null);
        assertThat(disciplina1).isNotEqualTo(disciplina2);
    }
}
