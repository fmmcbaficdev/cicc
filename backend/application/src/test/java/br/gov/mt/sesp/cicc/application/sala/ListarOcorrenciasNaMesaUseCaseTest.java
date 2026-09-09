package br.gov.mt.sesp.cicc.application.sala;

import br.gov.mt.sesp.cicc.application.cad.InMemoryOcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListarOcorrenciasNaMesaUseCaseTest {

    private InMemoryOcorrenciaRepository ocorrenciaRepository;
    private ListarOcorrenciasNaMesaUseCase useCase;

    @BeforeEach
    void setUp() {
        ocorrenciaRepository = new InMemoryOcorrenciaRepository();
        useCase = new ListarOcorrenciasNaMesaUseCase(ocorrenciaRepository);
    }

    @Test
    @DisplayName("lista só os cartões já encaminhados à mesa pedida")
    void listaDaMesa() {
        final var cuiaba = ocorrenciaRepository.criar(Ocorrencia.newOcorrencia(
                "Roubo", "Roubo", "CRITICA", "Centro, Cuiabá", -15.6, -56.1, "CICC-2026-SALA-001", Instant.parse("2026-09-07T22:30:00Z")
        ));
        cuiaba.encaminharAMesa("CBA", Instant.parse("2026-09-07T22:32:00Z"));
        ocorrenciaRepository.atualizar(cuiaba);

        final var rondonopolis = ocorrenciaRepository.criar(Ocorrencia.newOcorrencia(
                "Furto", "Furto", "BAIXA", "Centro, Rondonópolis", -16.4, -54.6, "CICC-2026-SALA-002", Instant.parse("2026-09-07T22:31:00Z")
        ));
        rondonopolis.encaminharAMesa("RDO", Instant.parse("2026-09-07T22:33:00Z"));
        ocorrenciaRepository.atualizar(rondonopolis);

        ocorrenciaRepository.criar(Ocorrencia.newOcorrencia(
                "Ameaça", "Ainda na triagem", "MEDIA", "CPA, Cuiabá", -15.5, -56.0, "CICC-2026-SALA-003", Instant.parse("2026-09-07T22:34:00Z")
        ));

        final var naMesa = useCase.execute(new ListarOcorrenciasNaMesaUseCase.Input("CBA"));

        assertEquals(1, naMesa.size());
        assertEquals("CICC-2026-SALA-001", naMesa.getFirst().protocolo());
        assertEquals("NA_MESA", naMesa.getFirst().situacao());
    }

    @Test
    @DisplayName("ordena a fila: Crítica na frente da Baixa")
    void ordenaPorGravidade() {
        final var baixa = ocorrenciaRepository.criar(Ocorrencia.newOcorrencia(
                "Furto", "Furto", "BAIXA", "Centro, Cuiabá", -15.6, -56.1, "CICC-2026-SALA-BAIXA", Instant.parse("2026-09-07T22:30:00Z")
        ));
        baixa.encaminharAMesa("CBA", Instant.parse("2026-09-07T22:32:00Z"));
        ocorrenciaRepository.atualizar(baixa);

        final var critica = ocorrenciaRepository.criar(Ocorrencia.newOcorrencia(
                "Roubo", "Roubo", "CRITICA", "CPA, Cuiabá", -15.5, -56.0, "CICC-2026-SALA-CRITICA", Instant.parse("2026-09-07T22:34:00Z")
        ));
        critica.encaminharAMesa("CBA", Instant.parse("2026-09-07T22:35:00Z"));
        ocorrenciaRepository.atualizar(critica);

        final var naMesa = useCase.execute(new ListarOcorrenciasNaMesaUseCase.Input("CBA"));

        assertEquals("CICC-2026-SALA-CRITICA", naMesa.getFirst().protocolo());
        assertEquals("CICC-2026-SALA-BAIXA", naMesa.get(1).protocolo());
    }
}
