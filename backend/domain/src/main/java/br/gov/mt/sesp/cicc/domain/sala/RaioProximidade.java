package br.gov.mt.sesp.cicc.domain.sala;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

public record RaioProximidade(int metros) {

    public static final RaioProximidade PERTO = new RaioProximidade(3_000);

    public RaioProximidade {
        if (metros <= 0) {
            throw new ValidationException("Raio de proximidade inválido");
        }
    }

    public boolean cobre(final int distanciaMetros) {
        return distanciaMetros <= metros;
    }
}
