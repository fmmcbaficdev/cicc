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

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SugerirViaturasUseCaseTest {

    private static final Instant T1 = Instant.parse("2026-09-07T22:30:00Z");

    private InMemoryOcorrenciaRepository ocorrencias;
    private InMemoryAvlPort avl;
    private SugerirViaturasUseCase useCase;

    @BeforeEach
    void setUp() {
        ocorrencias = new InMemoryOcorrenciaRepository();
        avl = new InMemoryAvlPort();
        avl.registrar(new Recurso("PM-CBA-01", new Ponto(-15.6018, -56.0982), "LIVRE"));
        avl.registrar(new Recurso("PM-CBA-02", new Ponto(-15.6080, -56.1050), "LIVRE"));
        avl.registrar(new Recurso("PM-VG-01", new Ponto(-15.6465, -56.1326), "LIVRE"));
        avl.registrar(new Recurso("PM-CBA-99", new Ponto(-15.6015, -56.0975), "EMPENHADA"));
        useCase = new SugerirViaturasUseCase(ocorrencias, avl);
    }

    @Test
    @DisplayName("sugere Livre mais próximas do ponto da ocorrência")
    void sugereMaisProximas() {
        final var id = naMesa().ocorrenciaId().value();

        final var sugestoes = useCase.execute(new SugerirViaturasUseCase.Input(id));

        assertEquals("PM-CBA-01", sugestoes.getFirst().prefixo());
        assertEquals("PM-CBA-02", sugestoes.get(1).prefixo());
        assertEquals(2, sugestoes.size());
    }

    @Test
    @DisplayName("ampliar puxa Livre de outro bairro ainda por proximidade")
    void ampliarPuxaOutroBairro() {
        avl = new InMemoryAvlPort();
        avl.registrar(new Recurso("PM-VG-01", new Ponto(-15.6465, -56.1326), "LIVRE"));
        useCase = new SugerirViaturasUseCase(ocorrencias, avl);
        final var id = naMesa().ocorrenciaId().value();

        assertTrue(useCase.execute(new SugerirViaturasUseCase.Input(id, false)).isEmpty());
        assertEquals("PM-VG-01", useCase.execute(new SugerirViaturasUseCase.Input(id, true)).getFirst().prefixo());
    }

    @Test
    @DisplayName("recusa sugerir antes do cartão estar na mesa")
    void recusaForaDaMesa() {
        final var aberta = ocorrencias.criar(Ocorrencia.newOcorrencia(
                "Roubo", "Roubo", "CRITICA", "Centro, Cuiabá", -15.601411, -56.097892, "CICC-2026-SUG-002", T1
        ));

        assertEquals(
                "Ocorrência ainda não está na mesa do despachador",
                assertThrows(
                        ValidationException.class,
                        () -> useCase.execute(new SugerirViaturasUseCase.Input(aberta.ocorrenciaId().value()))
                ).getMessage()
        );
    }

    @Test
    @DisplayName("AVL sem Livre devolve lista vazia")
    void avlSemLivre() {
        avl = new InMemoryAvlPort();
        useCase = new SugerirViaturasUseCase(ocorrencias, avl);
        final var id = naMesa().ocorrenciaId().value();

        assertTrue(useCase.execute(new SugerirViaturasUseCase.Input(id)).isEmpty());
    }

    private Ocorrencia naMesa() {
        final var aberta = ocorrencias.criar(Ocorrencia.newOcorrencia(
                "Roubo", "Roubo", "CRITICA", "Av. Historiador Rubens de Mendonça, Cuiabá",
                -15.601411, -56.097892, "CICC-2026-SUG-001", T1
        ));
        aberta.encaminharAMesa("CBA", T1.plusSeconds(60));
        return ocorrencias.atualizar(aberta);
    }
}
