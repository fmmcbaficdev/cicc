package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProtocoloTest {

    @Test
    @DisplayName("aceita protocolo não vazio no formato do órgão")
    void aceitaProtocoloValido() {
        assertEquals("CICC-2026-000001", new Protocolo(" CICC-2026-000001 ").value());
    }

    @Test
    @DisplayName("recusa protocolo vazio, nulo ou com caracteres inválidos")
    void recusaProtocoloInvalido() {
        assertEquals("Protocolo inválido",
                assertThrows(ValidationException.class, () -> new Protocolo(null)).getMessage());
        assertEquals("Protocolo inválido",
                assertThrows(ValidationException.class, () -> new Protocolo("  ")).getMessage());
        assertEquals("Protocolo inválido",
                assertThrows(ValidationException.class, () -> new Protocolo("ab")).getMessage());
        assertEquals("Protocolo inválido",
                assertThrows(ValidationException.class, () -> new Protocolo("CICC 2026")).getMessage());
    }
}
