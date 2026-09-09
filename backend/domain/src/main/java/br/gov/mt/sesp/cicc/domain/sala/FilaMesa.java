package br.gov.mt.sesp.cicc.domain.sala;

import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;

import java.util.Comparator;
import java.util.List;

public final class FilaMesa {

    private FilaMesa() {
    }

    public static List<Ocorrencia> ordenar(final List<Ocorrencia> cartoes) {
        if (cartoes == null || cartoes.isEmpty()) {
            return List.of();
        }
        return cartoes.stream()
                .sorted(Comparator
                        .comparingInt(FilaMesa::posicaoNaFila)
                        .thenComparingInt(ocorrencia -> ocorrencia.gravidade().ordemNaFila())
                        .thenComparing(Ocorrencia::inicioAtendimento))
                .toList();
    }

    private static int posicaoNaFila(final Ocorrencia ocorrencia) {
        if (ocorrencia.encerradaEm() != null) {
            return 3;
        }
        if (ocorrencia.noLocalEm() != null) {
            return 2;
        }
        if (ocorrencia.inicioDeslocamento() != null) {
            return 1;
        }
        return 0;
    }
}
