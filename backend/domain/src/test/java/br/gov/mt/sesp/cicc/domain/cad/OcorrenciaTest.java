package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OcorrenciaTest {

    private static final Instant T1 = Instant.parse("2026-09-07T22:00:00Z");

    @Test
    @DisplayName("ao abrir, registra o quê, onde, gravidade e carimba o T1 no clique")
    void abreComT1() {
        final var ocorrencia = abrir();

        assertNotNull(ocorrencia.ocorrenciaId());
        assertEquals("CICC-2026-000001", ocorrencia.protocolo().value());
        assertEquals("Roubo a mão armada agora", ocorrencia.descricao());
        assertEquals("CRITICA", ocorrencia.gravidade().value());
        assertEquals("Av. Historiador Rubens de Mendonça, Cuiabá", ocorrencia.endereco());
        assertEquals(-15.601411, ocorrencia.ponto().latitude());
        assertEquals(-56.097892, ocorrencia.ponto().longitude());
        assertEquals(T1, ocorrencia.inicioAtendimento());
    }

    @Test
    @DisplayName("restore rehidrata o mesmo T1 sem gerar id novo")
    void restoreMantemIdentidadeECarimbo() {
        final var original = abrir();

        final var rehidratada = Ocorrencia.restore(
                original.ocorrenciaId(),
                original.protocolo(),
                original.descricao(),
                original.gravidade(),
                original.endereco(),
                original.ponto(),
                original.inicioAtendimento()
        );

        assertEquals(original, rehidratada);
        assertEquals(T1, rehidratada.inicioAtendimento());
    }

    @Test
    @DisplayName("equals e hashCode consideram só o id")
    void igualdadeSoPeloId() {
        final var id = OcorrenciaId.with("mesma-ocorrencia");
        final var uma = Ocorrencia.restore(
                id,
                new Protocolo("CICC-2026-000001"),
                "Furto passado",
                new Gravidade("BAIXA"),
                "Centro, Cuiabá",
                new Ponto(-15.6, -56.1),
                T1
        );
        final var outra = Ocorrencia.restore(
                id,
                new Protocolo("CICC-2026-000099"),
                "Outra descrição",
                new Gravidade("ALTA"),
                "CPA, Cuiabá",
                new Ponto(-15.5, -56.0),
                T1.plusSeconds(60)
        );

        assertEquals(uma, outra);
        assertEquals(uma.hashCode(), outra.hashCode());
        assertNotEquals(uma, abrir());
    }

    @Test
    @DisplayName("classificar troca a gravidade sem alterar o T1")
    void classificaGravidade() {
        final var ocorrencia = abrir();

        ocorrencia.classificar("MEDIA");

        assertEquals("MEDIA", ocorrencia.gravidade().value());
        assertEquals(T1, ocorrencia.inicioAtendimento());
    }

    @Test
    @DisplayName("telefone e uid do PABX são opcionais e não alteram o T1")
    void guardaTelefoneSemMudarT1() {
        final var ocorrencia = Ocorrencia.newOcorrencia(
                "Roubo a mão armada agora",
                "CRITICA",
                "Av. Historiador Rubens de Mendonça, Cuiabá",
                -15.601411,
                -56.097892,
                "CICC-2026-000001",
                T1,
                new Telefone("65981234567"),
                "pabx-mock-190-cba-001"
        );

        assertEquals("65981234567", ocorrencia.telefone().value());
        assertEquals("pabx-mock-190-cba-001", ocorrencia.pabxUid());
        assertEquals(T1, ocorrencia.inicioAtendimento());
    }

    @Test
    @DisplayName("recusa abertura sem T1, descrição ou endereço")
    void recusaInvariantesDaAbertura() {
        assertEquals(
                "Início do atendimento (T1) é obrigatório",
                assertThrows(ValidationException.class, () -> Ocorrencia.newOcorrencia(
                        "Roubo", "CRITICA", "Centro, Cuiabá", -15.6, -56.1, "CICC-2026-000001", null
                )).getMessage()
        );
        assertEquals(
                "Descrição da ocorrência é obrigatória",
                assertThrows(ValidationException.class, () -> Ocorrencia.newOcorrencia(
                        "  ", "CRITICA", "Centro, Cuiabá", -15.6, -56.1, "CICC-2026-000001", T1
                )).getMessage()
        );
        assertEquals(
                "Endereço da ocorrência é obrigatório",
                assertThrows(ValidationException.class, () -> Ocorrencia.newOcorrencia(
                        "Roubo", "CRITICA", "", -15.6, -56.1, "CICC-2026-000001", T1
                )).getMessage()
        );
    }

    private static Ocorrencia abrir() {
        return Ocorrencia.newOcorrencia(
                "Roubo a mão armada agora",
                "CRITICA",
                "Av. Historiador Rubens de Mendonça, Cuiabá",
                -15.601411,
                -56.097892,
                "CICC-2026-000001",
                T1
        );
    }
}
