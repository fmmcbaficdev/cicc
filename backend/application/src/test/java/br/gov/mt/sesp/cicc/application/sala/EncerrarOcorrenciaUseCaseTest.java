package br.gov.mt.sesp.cicc.application.sala;

import br.gov.mt.sesp.cicc.application.avl.InMemoryAvlPort;
import br.gov.mt.sesp.cicc.application.cad.InMemoryOcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.avl.Recurso;
import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;
import br.gov.mt.sesp.cicc.domain.cad.Ponto;
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

class EncerrarOcorrenciaUseCaseTest {

    private static final Instant T1 = Instant.parse("2026-09-07T22:30:00Z");
    private static final Instant T2 = Instant.parse("2026-09-07T22:35:00Z");
    private static final Instant NO_LOCAL = Instant.parse("2026-09-07T22:48:00Z");
    private static final Instant FIM = Instant.parse("2026-09-07T23:00:00Z");

    private InMemoryOcorrenciaRepository ocorrencias;
    private InMemoryAvlPort avl;
    private EncerrarOcorrenciaUseCase useCase;

    @BeforeEach
    void setUp() {
        ocorrencias = new InMemoryOcorrenciaRepository();
        avl = new InMemoryAvlPort();
        avl.registrar(new Recurso("PM-CBA-01", new Ponto(-15.6018, -56.0982), "LIVRE"));
        useCase = new EncerrarOcorrenciaUseCase(ocorrencias, avl, Clock.fixed(FIM, ZoneOffset.UTC));
    }

    @Test
    @DisplayName("encerra o caso e devolve a viatura a Livre")
    void encerraELibera() {
        final var id = noLocal().ocorrenciaId().value();

        final var output = useCase.execute(new EncerrarOcorrenciaUseCase.Input(id));

        assertEquals("ENCERRADA", output.situacao());
        assertEquals(FIM, output.encerradaEm());
        assertEquals(T1, output.inicioAtendimento());
        assertTrue(avl.viaturaDePrefixo("PM-CBA-01").orElseThrow().livre());
    }

    @Test
    @DisplayName("recusa encerrar sem No local")
    void recusaSemNoLocal() {
        final var aberta = ocorrencias.criar(Ocorrencia.newOcorrencia(
                "Roubo", "CRITICA", "Centro, Cuiabá", -15.601411, -56.097892, "CICC-2026-ENC-002", T1
        ));
        aberta.encaminharAMesa("CBA", T1.plusSeconds(60));
        ocorrencias.atualizar(aberta);

        assertEquals(
                "Ocorrência ainda não está no local",
                assertThrows(
                        ValidationException.class,
                        () -> useCase.execute(new EncerrarOcorrenciaUseCase.Input(aberta.ocorrenciaId().value()))
                ).getMessage()
        );
    }

    private Ocorrencia noLocal() {
        final var aberta = ocorrencias.criar(Ocorrencia.newOcorrencia(
                "Roubo", "CRITICA", "Centro, Cuiabá", -15.601411, -56.097892, "CICC-2026-ENC-001", T1
        ));
        aberta.encaminharAMesa("CBA", T1.plusSeconds(60));
        aberta.empenhar("PM-CBA-01", T2);
        avl.empenhar("PM-CBA-01");
        aberta.registrarNoLocal(NO_LOCAL, true);
        return ocorrencias.atualizar(aberta);
    }
}
