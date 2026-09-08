package br.gov.mt.sesp.cicc.domain.sala;

import br.gov.mt.sesp.cicc.domain.avl.Recurso;
import br.gov.mt.sesp.cicc.domain.cad.Ponto;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

import java.util.Comparator;
import java.util.List;

public final class MatchingProximidade {

    static final int LIMITE = 3;

    private MatchingProximidade() {
    }

    public static List<SugestaoViatura> maisProximas(final Ponto alvo, final List<Recurso> livres) {
        return maisProximas(alvo, livres, RaioProximidade.PERTO);
    }

    public static List<SugestaoViatura> ampliadas(final Ponto alvo, final List<Recurso> livres) {
        return maisProximas(alvo, livres, null);
    }

    static List<SugestaoViatura> maisProximas(
            final Ponto alvo,
            final List<Recurso> livres,
            final RaioProximidade raio
    ) {
        if (alvo == null) {
            throw new ValidationException("Ponto da ocorrência é obrigatório");
        }
        if (livres == null || livres.isEmpty()) {
            return List.of();
        }
        return livres.stream()
                .filter(Recurso::livre)
                .map(recurso -> new SugestaoViatura(
                        recurso.prefixo(),
                        recurso.ponto(),
                        (int) Math.round(alvo.distanciaEmMetros(recurso.ponto()))
                ))
                .filter(sugestao -> raio == null || raio.cobre(sugestao.distanciaMetros()))
                .sorted(Comparator.comparingInt(SugestaoViatura::distanciaMetros)
                        .thenComparing(SugestaoViatura::prefixo))
                .limit(LIMITE)
                .toList();
    }
}
