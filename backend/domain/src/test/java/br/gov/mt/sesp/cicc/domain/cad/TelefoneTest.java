package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TelefoneTest {

    @Test
    @DisplayName("aceita telefone com 10 ou 11 dígitos e normaliza a máscara")
    void aceitaTelefoneValido() {
        assertEquals("65981234567", new Telefone("(65) 98123-4567").value());
        assertEquals("6533211100", new Telefone("65 3321-1100").value());
    }

    @Test
    @DisplayName("recusa telefone vazio, nulo ou com tamanho inválido")
    void recusaTelefoneInvalido() {
        assertEquals("Telefone inválido",
                assertThrows(ValidationException.class, () -> new Telefone(null)).getMessage());
        assertEquals("Telefone inválido",
                assertThrows(ValidationException.class, () -> new Telefone("  ")).getMessage());
        assertEquals("Telefone inválido",
                assertThrows(ValidationException.class, () -> new Telefone("98123")).getMessage());
    }
}
