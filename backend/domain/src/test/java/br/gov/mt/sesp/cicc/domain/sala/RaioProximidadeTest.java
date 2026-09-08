package br.gov.mt.sesp.cicc.domain.sala;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RaioProximidadeTest {

    @Test
    @DisplayName("perto cobre 3 km e recusa raio nulo")
    void pertoTresQuilometros() {
        assertEquals(3_000, RaioProximidade.PERTO.metros());
        assertTrue(RaioProximidade.PERTO.cobre(3_000));
        assertFalse(RaioProximidade.PERTO.cobre(3_001));
        assertEquals(
                "Raio de proximidade inválido",
                assertThrows(ValidationException.class, () -> new RaioProximidade(0)).getMessage()
        );
    }
}
