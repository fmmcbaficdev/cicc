package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;
import br.gov.mt.sesp.cicc.domain.exception.NotFoundException;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EncaminharOcorrenciaUseCaseTest {

    private static final Instant CLIQUE = Instant.parse("2026-09-07T22:30:00Z");
    private static final Instant ENCAMINHA = Instant.parse("2026-09-07T22:32:00Z");

    private InMemoryOcorrenciaRepository ocorrenciaRepository;
    private EncaminharOcorrenciaUseCase useCase;

    @BeforeEach
    void setUp() {
        ocorrenciaRepository = new InMemoryOcorrenciaRepository();
        useCase = new EncaminharOcorrenciaUseCase(ocorrenciaRepository, Clock.fixed(ENCAMINHA, ZoneOffset.UTC));
    }

    @Test
    @DisplayName("deixa o cartão disponível na mesa sem alterar o T1")
    void encaminhaParaMesa() {
        final var aberta = ocorrenciaRepository.criar(Ocorrencia.newOcorrencia(
                "Roubo", "Roubo", "CRITICA", "Centro, Cuiabá", -15.6, -56.1, "CICC-2026-MESA-001", CLIQUE
        ));

        final var output = useCase.execute(new EncaminharOcorrenciaUseCase.Input(aberta.ocorrenciaId().value(), "CBA"));

        assertEquals("NA_MESA", output.situacao());
        assertEquals("CBA", output.mesa());
        assertEquals(ENCAMINHA, output.encaminhadaEm());
        assertEquals(CLIQUE, output.inicioAtendimento());
        assertEquals(-15.6, output.latitude());
    }

    @Test
    @DisplayName("recusa encaminhar de novo")
    void recusaSegundoEncaminhamento() {
        final var aberta = ocorrenciaRepository.criar(Ocorrencia.newOcorrencia(
                "Roubo", "Roubo", "CRITICA", "Centro, Cuiabá", -15.6, -56.1, "CICC-2026-MESA-002", CLIQUE
        ));
        useCase.execute(new EncaminharOcorrenciaUseCase.Input(aberta.ocorrenciaId().value(), "CBA"));

        final var erro = assertThrows(
                ValidationException.class,
                () -> useCase.execute(new EncaminharOcorrenciaUseCase.Input(aberta.ocorrenciaId().value(), "VG"))
        );
        assertEquals("Ocorrência já está na mesa do despachador", erro.getMessage());
    }

    @Test
    @DisplayName("id desconhecido não inventa cartão")
    void naoEncontra() {
        assertThrows(
                NotFoundException.class,
                () -> useCase.execute(new EncaminharOcorrenciaUseCase.Input("inexistente", "CBA"))
        );
    }
}
