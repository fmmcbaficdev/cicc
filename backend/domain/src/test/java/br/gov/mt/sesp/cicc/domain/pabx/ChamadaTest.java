package br.gov.mt.sesp.cicc.domain.pabx;

import br.gov.mt.sesp.cicc.domain.cad.Telefone;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChamadaTest {

    private static final Instant INSTANTE = Instant.parse("2026-09-07T22:00:00Z");

    @Test
    @DisplayName("guarda o contrato da ligação: uid, telefone, tronco, unidade e instante")
    void aceitaContrato() {
        final var chamada = new Chamada(
                " pabx-mock-190-cba-001 ",
                new Telefone("65981234567"),
                "190",
                "CBA",
                INSTANTE
        );

        assertEquals("pabx-mock-190-cba-001", chamada.uid());
        assertEquals("65981234567", chamada.telefone().value());
        assertEquals("190", chamada.tronco());
        assertEquals("CBA", chamada.unidade());
        assertEquals(INSTANTE, chamada.instante());
    }

    @Test
    @DisplayName("recusa ligação sem uid ou sem telefone")
    void recusaContratoIncompleto() {
        assertEquals("Uid da ligação inválido",
                assertThrows(ValidationException.class, () -> new Chamada(
                        "  ", new Telefone("65981234567"), "190", "CBA", INSTANTE
                )).getMessage());
        assertEquals("Telefone da ligação é obrigatório",
                assertThrows(ValidationException.class, () -> new Chamada(
                        "pabx-1", null, "190", "CBA", INSTANTE
                )).getMessage());
    }
}
