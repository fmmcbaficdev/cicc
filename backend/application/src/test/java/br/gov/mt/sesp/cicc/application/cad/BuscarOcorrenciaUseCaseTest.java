package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuscarOcorrenciaUseCaseTest {

    private InMemoryOcorrenciaRepository ocorrenciaRepository;
    private BuscarOcorrenciaUseCase useCase;

    @BeforeEach
    void setUp() {
        ocorrenciaRepository = new InMemoryOcorrenciaRepository();
        useCase = new BuscarOcorrenciaUseCase(ocorrenciaRepository);
    }

    @Test
    @DisplayName("devolve a ocorrência persistida pelo id")
    void encontraPorId() {
        final var persistida = ocorrenciaRepository.criar(Ocorrencia.newOcorrencia(
                "Roubo",
                "CRITICA",
                "Centro, Cuiabá",
                -15.6,
                -56.1,
                "CICC-2026-BUSCA-001",
                Instant.parse("2026-09-07T22:30:00Z")
        ));

        final var output = useCase.execute(new BuscarOcorrenciaUseCase.Input(persistida.ocorrenciaId().value()));

        assertTrue(output.isPresent());
        assertEquals("CICC-2026-BUSCA-001", output.get().protocolo());
        assertEquals(Instant.parse("2026-09-07T22:30:00Z"), output.get().inicioAtendimento());
    }

    @Test
    @DisplayName("id desconhecido fica vazio")
    void naoEncontra() {
        final var output = useCase.execute(new BuscarOcorrenciaUseCase.Input("inexistente"));

        assertTrue(output.isEmpty());
    }
}
