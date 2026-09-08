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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmpenharViaturaUseCaseTest {

    private static final Instant T1 = Instant.parse("2026-09-07T22:30:00Z");
    private static final Instant T2 = Instant.parse("2026-09-07T22:35:00Z");

    private InMemoryOcorrenciaRepository ocorrencias;
    private InMemoryAvlPort avl;
    private EmpenharViaturaUseCase useCase;

    @BeforeEach
    void setUp() {
        ocorrencias = new InMemoryOcorrenciaRepository();
        avl = new InMemoryAvlPort();
        avl.registrar(new Recurso("PM-CBA-01", new Ponto(-15.6018, -56.0982), "LIVRE"));
        useCase = new EmpenharViaturaUseCase(ocorrencias, avl, Clock.fixed(T2, ZoneOffset.UTC));
    }

    @Test
    @DisplayName("confirma o empenho, carimba o T2 e tira a viatura de Livre")
    void empenhaECarimbaT2() {
        final var id = naMesa().ocorrenciaId().value();

        final var output = useCase.execute(new EmpenharViaturaUseCase.Input(id, "PM-CBA-01"));

        assertEquals("EMPENHADA", output.situacao());
        assertEquals("PM-CBA-01", output.prefixoEmpenhado());
        assertEquals(T2, output.inicioDeslocamento());
        assertEquals(T1, output.inicioAtendimento());
        assertFalse(avl.viaturaDePrefixo("PM-CBA-01").orElseThrow().livre());
    }

    @Test
    @DisplayName("recusa prefixo que não está Livre")
    void recusaNaoLivre() {
        avl.empenhar("PM-CBA-01");
        final var id = naMesa().ocorrenciaId().value();

        assertEquals(
                "Viatura não está Livre",
                assertThrows(
                        ValidationException.class,
                        () -> useCase.execute(new EmpenharViaturaUseCase.Input(id, "PM-CBA-01"))
                ).getMessage()
        );
    }

    private Ocorrencia naMesa() {
        final var aberta = ocorrencias.criar(Ocorrencia.newOcorrencia(
                "Roubo", "CRITICA", "Centro, Cuiabá", -15.601411, -56.097892, "CICC-2026-EMP-001", T1
        ));
        aberta.encaminharAMesa("CBA", T1.plusSeconds(60));
        return ocorrencias.atualizar(aberta);
    }
}
