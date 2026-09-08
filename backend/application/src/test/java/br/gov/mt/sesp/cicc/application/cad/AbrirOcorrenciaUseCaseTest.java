package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaId;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbrirOcorrenciaUseCaseTest {

    private static final Instant CLIQUE = Instant.parse("2026-09-07T22:30:00Z");

    private InMemoryOcorrenciaRepository ocorrenciaRepository;
    private AbrirOcorrenciaUseCase useCase;

    @BeforeEach
    void setUp() {
        ocorrenciaRepository = new InMemoryOcorrenciaRepository();
        useCase = new AbrirOcorrenciaUseCase(ocorrenciaRepository, Clock.fixed(CLIQUE, ZoneOffset.UTC));
    }

    @Test
    @DisplayName("abre à mão e carimba o T1 no clique")
    void abreAMaoComT1() {
        final var input = inputValido("CICC-2026-000001");

        final var output = useCase.execute(input);

        assertNotNull(output.id());
        assertEquals("CICC-2026-000001", output.protocolo());
        assertEquals(CLIQUE, output.inicioAtendimento());

        final var persistida = ocorrenciaRepository.ocorrenciaDeId(OcorrenciaId.with(output.id())).orElseThrow();
        assertEquals("CRITICA", persistida.gravidade().value());
        assertEquals("Av. Historiador Rubens de Mendonça, Cuiabá", persistida.endereco());
        assertEquals(-15.601411, persistida.ponto().latitude());
    }

    @Test
    @DisplayName("recusa protocolo já existente")
    void recusaProtocoloDuplicado() {
        ocorrenciaRepository.criar(Ocorrencia.newOcorrencia(
                "Furto passado",
                "BAIXA",
                "Centro, Cuiabá",
                -15.6,
                -56.1,
                "CICC-2026-000001",
                CLIQUE.minusSeconds(60)
        ));

        final var erro = assertThrows(ValidationException.class, () -> useCase.execute(inputValido("CICC-2026-000001")));

        assertEquals("Já existe ocorrência com este protocolo", erro.getMessage());
    }

    @Test
    @DisplayName("recusa gravidade fora da escala oficial")
    void recusaGravidadeInvalida() {
        final var input = new AbrirOcorrenciaUseCase.Input(
                "Roubo a mão armada agora",
                "ALTO",
                "Av. Historiador Rubens de Mendonça, Cuiabá",
                -15.601411,
                -56.097892,
                "CICC-2026-000002"
        );

        final var erro = assertThrows(ValidationException.class, () -> useCase.execute(input));

        assertEquals("Gravidade inválida", erro.getMessage());
    }

    private static AbrirOcorrenciaUseCase.Input inputValido(final String protocolo) {
        return new AbrirOcorrenciaUseCase.Input(
                "Roubo a mão armada agora",
                "CRITICA",
                "Av. Historiador Rubens de Mendonça, Cuiabá",
                -15.601411,
                -56.097892,
                protocolo
        );
    }
}
