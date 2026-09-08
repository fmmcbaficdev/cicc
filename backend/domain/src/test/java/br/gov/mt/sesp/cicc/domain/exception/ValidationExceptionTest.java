package br.gov.mt.sesp.cicc.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationExceptionTest {

    @Test
    @DisplayName("guarda a mensagem de regra em português")
    void guardaMensagem() {
        var erro = assertThrows(ValidationException.class, () -> {
            throw new ValidationException("Gravidade inválida");
        });
        assertEquals("Gravidade inválida", erro.getMessage());
    }
}
