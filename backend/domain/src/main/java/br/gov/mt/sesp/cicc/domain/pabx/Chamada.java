package br.gov.mt.sesp.cicc.domain.pabx;

import br.gov.mt.sesp.cicc.domain.cad.Telefone;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

import java.time.Instant;

public record Chamada(String uid, Telefone telefone, String tronco, String unidade, Instant instante) {

    public Chamada {
        if (uid == null || uid.isBlank()) {
            throw new ValidationException("Uid da ligação inválido");
        }
        if (telefone == null) {
            throw new ValidationException("Telefone da ligação é obrigatório");
        }
        if (tronco == null || tronco.isBlank()) {
            throw new ValidationException("Tronco da ligação é obrigatório");
        }
        if (unidade == null || unidade.isBlank()) {
            throw new ValidationException("Unidade da ligação é obrigatória");
        }
        if (instante == null) {
            throw new ValidationException("Instante da ligação é obrigatório");
        }
        uid = uid.trim();
        tronco = tronco.trim();
        unidade = unidade.trim();
    }
}
