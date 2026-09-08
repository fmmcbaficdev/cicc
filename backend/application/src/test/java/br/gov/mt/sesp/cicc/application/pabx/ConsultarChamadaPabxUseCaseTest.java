package br.gov.mt.sesp.cicc.application.pabx;

import br.gov.mt.sesp.cicc.domain.cad.Telefone;
import br.gov.mt.sesp.cicc.domain.pabx.Chamada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsultarChamadaPabxUseCaseTest {

    private static final Instant INSTANTE = Instant.parse("2026-09-07T22:40:00Z");

    private InMemoryPabxPort pabxPort;
    private ConsultarChamadaPabxUseCase useCase;

    @BeforeEach
    void setUp() {
        pabxPort = new InMemoryPabxPort();
        useCase = new ConsultarChamadaPabxUseCase(pabxPort);
    }

    @Test
    @DisplayName("devolve uid e telefone quando o mock tem ligação na mesa")
    void devolveChamadaAtual() {
        pabxPort.definirChamadaAtual(new Chamada(
                "pabx-mock-190-cba-001",
                new Telefone("65981234567"),
                "190",
                "CBA",
                INSTANTE
        ));

        final var output = useCase.execute();

        assertTrue(output.isPresent());
        assertEquals("pabx-mock-190-cba-001", output.get().uid());
        assertEquals("65981234567", output.get().telefone());
        assertEquals("190", output.get().tronco());
        assertEquals("CBA", output.get().unidade());
        assertEquals(INSTANTE, output.get().instante());
    }

    @Test
    @DisplayName("PABX mudo devolve vazio e não impede o atendimento")
    void pabxMudoFicaVazio() {
        pabxPort.silenciar();

        assertTrue(useCase.execute().isEmpty());
    }
}
