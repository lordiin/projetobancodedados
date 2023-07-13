package projetobancodedados.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import projetobancodedados.app.web.rest.TestUtil;

class DepartamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Departamento.class);
        Departamento departamento1 = new Departamento();
        departamento1.setId(1L);
        Departamento departamento2 = new Departamento();
        departamento2.setId(departamento1.getId());
        assertThat(departamento1).isEqualTo(departamento2);
        departamento2.setId(2L);
        assertThat(departamento1).isNotEqualTo(departamento2);
        departamento1.setId(null);
        assertThat(departamento1).isNotEqualTo(departamento2);
    }
}
