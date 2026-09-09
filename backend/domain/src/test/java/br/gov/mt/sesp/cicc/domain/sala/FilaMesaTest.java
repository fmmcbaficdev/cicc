package br.gov.mt.sesp.cicc.domain.sala;

import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilaMesaTest {

    private static final Instant T1 = Instant.parse("2026-09-07T22:30:00Z");

    @Test
    @DisplayName("Crítica na frente da Baixa; Empenhada no fim da fila")
    void criticaAntesDeBaixa() {
        final var baixa = naMesa("Furto", "BAIXA", "CICC-2026-FILA-BAIXA", T1);
        final var critica = naMesa("Roubo", "CRITICA", "CICC-2026-FILA-CRITICA", T1.plusSeconds(30));
        critica.empenhar("PM-CBA-01", T1.plusSeconds(90));

        final var soNaMesa = naMesa("Agressão", "CRITICA", "CICC-2026-FILA-ABERTA", T1.plusSeconds(60));

        final var fila = FilaMesa.ordenar(List.of(baixa, critica, soNaMesa));

        assertEquals("CICC-2026-FILA-ABERTA", fila.get(0).protocolo().value());
        assertEquals("CICC-2026-FILA-BAIXA", fila.get(1).protocolo().value());
        assertEquals("CICC-2026-FILA-CRITICA", fila.get(2).protocolo().value());
    }

    private static Ocorrencia naMesa(
            final String descricao,
            final String gravidade,
            final String protocolo,
            final Instant t1
    ) {
        final var ocorrencia = Ocorrencia.newOcorrencia(
                descricao, descricao, gravidade, "Centro, Cuiabá", -15.601411, -56.097892, protocolo, t1
        );
        ocorrencia.encaminharAMesa("CBA", t1.plusSeconds(10));
        return ocorrencia;
    }
}
