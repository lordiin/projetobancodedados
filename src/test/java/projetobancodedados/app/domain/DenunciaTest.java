package projetobancodedados.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import projetobancodedados.app.web.rest.TestUtil;

class DenunciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Denuncia.class);
        Denuncia denuncia1 = new Denuncia();
        denuncia1.setId(1L);
        Denuncia denuncia2 = new Denuncia();
        denuncia2.setId(denuncia1.getId());
        assertThat(denuncia1).isEqualTo(denuncia2);
        denuncia2.setId(2L);
        assertThat(denuncia1).isNotEqualTo(denuncia2);
        denuncia1.setId(null);
        assertThat(denuncia1).isNotEqualTo(denuncia2);
    }
}
