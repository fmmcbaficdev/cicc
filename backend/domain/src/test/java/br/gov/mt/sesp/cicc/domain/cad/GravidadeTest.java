package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GravidadeTest {

    @Test
    @DisplayName("aceita as quatro gravidades oficiais")
    void aceitaEscalaOficial() {
        assertEquals("BAIXA", new Gravidade("baixa").value());
        assertEquals("MEDIA", new Gravidade("MEDIA").value());
        assertEquals("ALTA", new Gravidade("Alta").value());
        assertEquals("CRITICA", new Gravidade("CRITICA").value());
    }

    @Test
    @DisplayName("recusa a escala ALTA/MEDIA/BAIXA de três níveis e valor nulo")
    void recusaEscalaInvalida() {
        assertEquals("Gravidade inválida",
                assertThrows(ValidationException.class, () -> new Gravidade("ALTO")).getMessage());
        assertEquals("Gravidade inválida",
                assertThrows(ValidationException.class, () -> new Gravidade(null)).getMessage());
        assertEquals("Gravidade inválida",
                assertThrows(ValidationException.class, () -> new Gravidade("P1")).getMessage());
    }
}
