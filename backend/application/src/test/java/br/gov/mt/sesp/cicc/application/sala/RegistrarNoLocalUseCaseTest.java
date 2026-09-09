package br.gov.mt.sesp.cicc.application.sala;

import br.gov.mt.sesp.cicc.application.cad.InMemoryOcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrarNoLocalUseCaseTest {

    private static final Instant T1 = Instant.parse("2026-09-07T22:30:00Z");
    private static final Instant T2 = Instant.parse("2026-09-07T22:35:00Z");
    private static final Instant NO_LOCAL = Instant.parse("2026-09-07T22:48:00Z");

    private InMemoryOcorrenciaRepository ocorrencias;
    private RegistrarNoLocalUseCase useCase;

    @BeforeEach
    void setUp() {
        ocorrencias = new InMemoryOcorrenciaRepository();
        useCase = new RegistrarNoLocalUseCase(ocorrencias, Clock.fixed(NO_LOCAL, ZoneOffset.UTC));
    }

    @Test
    @DisplayName("fecha o T2 no clique e marca o carimbo como manual")
    void fechaT2Manual() {
        final var id = empenhada().ocorrenciaId().value();

        final var output = useCase.execute(new RegistrarNoLocalUseCase.Input(id));

        assertEquals("NO_LOCAL", output.situacao());
        assertEquals(NO_LOCAL, output.noLocalEm());
        assertTrue(output.noLocalManual());
        assertEquals(T2, output.inicioDeslocamento());
        assertEquals(T1, output.inicioAtendimento());
    }

    @Test
    @DisplayName("recusa No local sem empenho")
    void recusaSemEmpenho() {
        final var aberta = ocorrencias.criar(Ocorrencia.newOcorrencia(
                "Roubo", "Roubo", "CRITICA", "Centro, Cuiabá", -15.601411, -56.097892, "CICC-2026-NL-002", T1
        ));
        aberta.encaminharAMesa("CBA", T1.plusSeconds(60));
        ocorrencias.atualizar(aberta);

        assertEquals(
                "Ocorrência ainda não tem viatura empenhada",
                assertThrows(
                        ValidationException.class,
                        () -> useCase.execute(new RegistrarNoLocalUseCase.Input(aberta.ocorrenciaId().value()))
                ).getMessage()
        );
    }

    private Ocorrencia empenhada() {
        final var aberta = ocorrencias.criar(Ocorrencia.newOcorrencia(
                "Roubo", "Roubo", "CRITICA", "Centro, Cuiabá", -15.601411, -56.097892, "CICC-2026-NL-001", T1
        ));
        aberta.encaminharAMesa("CBA", T1.plusSeconds(60));
        aberta.empenhar("PM-CBA-01", T2);
        return ocorrencias.atualizar(aberta);
    }
}
