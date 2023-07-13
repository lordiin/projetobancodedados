package projetobancodedados.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import projetobancodedados.app.web.rest.TestUtil;

class AvaliacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Avaliacao.class);
        Avaliacao avaliacao1 = new Avaliacao();
        avaliacao1.setId(1L);
        Avaliacao avaliacao2 = new Avaliacao();
        avaliacao2.setId(avaliacao1.getId());
        assertThat(avaliacao1).isEqualTo(avaliacao2);
        avaliacao2.setId(2L);
        assertThat(avaliacao1).isNotEqualTo(avaliacao2);
        avaliacao1.setId(null);
        assertThat(avaliacao1).isNotEqualTo(avaliacao2);
    }
}
