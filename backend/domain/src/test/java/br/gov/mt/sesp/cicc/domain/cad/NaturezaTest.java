package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NaturezaTest {

    @Test
    @DisplayName("aceita o tipo informado pelo atendente")
    void aceitaTexto() {
        assertEquals("Roubo", new Natureza("  Roubo  ").value());
    }

    @Test
    @DisplayName("recusa vazio ou acima de 80 caracteres")
    void recusaInvalida() {
        assertEquals(
                "Natureza da ocorrência é obrigatória",
                assertThrows(ValidationException.class, () -> new Natureza("  ")).getMessage()
        );
        assertEquals(
                "Natureza da ocorrência é obrigatória",
                assertThrows(ValidationException.class, () -> new Natureza(null)).getMessage()
        );
        assertEquals(
                "Natureza da ocorrência deve ter no máximo 80 caracteres",
                assertThrows(ValidationException.class, () -> new Natureza("R".repeat(81))).getMessage()
        );
    }
}
