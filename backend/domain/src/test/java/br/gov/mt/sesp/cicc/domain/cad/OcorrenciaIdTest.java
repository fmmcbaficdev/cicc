package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OcorrenciaIdTest {

    @Test
    @DisplayName("unique gera identificador e with rehidrata o mesmo valor")
    void uniqueEWith() {
        final var gerado = OcorrenciaId.unique();
        final var rehidratado = OcorrenciaId.with(gerado.value());

        assertEquals(gerado, rehidratado);
        assertNotEquals(gerado, OcorrenciaId.unique());
    }

    @Test
    @DisplayName("recusa identificador em branco")
    void recusaEmBranco() {
        assertEquals("Identificador da ocorrência inválido",
                assertThrows(ValidationException.class, () -> OcorrenciaId.with(" ")).getMessage());
    }
}
