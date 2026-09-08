package br.gov.mt.sesp.cicc.domain.sala;

import br.gov.mt.sesp.cicc.domain.avl.Recurso;
import br.gov.mt.sesp.cicc.domain.cad.Ponto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchingProximidadeTest {

    private static final Ponto ALVO = new Ponto(-15.601411, -56.097892);

    @Test
    @DisplayName("ordena Livre por distância e ignora Empenhada")
    void ordenaMaisProximas() {
        final var perto = new Recurso("PM-CBA-01", new Ponto(-15.6018, -56.0982), "LIVRE");
        final var longe = new Recurso("PM-CBA-03", new Ponto(-15.5700, -56.0700), "LIVRE");
        final var media = new Recurso("PM-CBA-02", new Ponto(-15.6080, -56.1050), "LIVRE");
        final var empenhada = new Recurso("PM-CBA-99", new Ponto(-15.6015, -56.0975), "EMPENHADA");

        final var sugestoes = MatchingProximidade.maisProximas(ALVO, List.of(longe, empenhada, perto, media));

        assertEquals(3, sugestoes.size());
        assertEquals("PM-CBA-01", sugestoes.getFirst().prefixo());
        assertEquals("PM-CBA-02", sugestoes.get(1).prefixo());
        assertEquals("PM-CBA-03", sugestoes.get(2).prefixo());
        assertTrue(sugestoes.getFirst().distanciaMetros() < sugestoes.get(1).distanciaMetros());
    }

    @Test
    @DisplayName("sem Livre devolve lista vazia")
    void semLivre() {
        assertTrue(MatchingProximidade.maisProximas(ALVO, List.of()).isEmpty());
    }
}
