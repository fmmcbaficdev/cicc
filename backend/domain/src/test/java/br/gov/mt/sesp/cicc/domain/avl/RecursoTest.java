package br.gov.mt.sesp.cicc.domain.avl;

import br.gov.mt.sesp.cicc.domain.cad.Ponto;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecursoTest {

    @Test
    @DisplayName("Livre entra no matching; Empenhada não")
    void livreOuEmpenhada() {
        final var livre = new Recurso("PM-CBA-01", new Ponto(-15.6, -56.1), "LIVRE");
        final var empenhada = new Recurso("PM-CBA-99", new Ponto(-15.6, -56.1), "EMPENHADA");

        assertTrue(livre.livre());
        assertFalse(empenhada.livre());
    }

    @Test
    @DisplayName("recusa prefixo vazio ou situação fora de Livre/Empenhada")
    void recusaInvariantes() {
        assertEquals(
                "Prefixo da viatura é obrigatório",
                assertThrows(ValidationException.class, () -> new Recurso(" ", new Ponto(-15.6, -56.1), "LIVRE"))
                        .getMessage()
        );
        assertEquals(
                "Situação da viatura inválida",
                assertThrows(ValidationException.class, () -> new Recurso("PM-CBA-01", new Ponto(-15.6, -56.1), "FORA"))
                        .getMessage()
        );
    }
}
