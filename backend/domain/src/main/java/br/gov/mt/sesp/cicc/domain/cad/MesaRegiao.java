package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

import java.util.Locale;
import java.util.Set;

public record MesaRegiao(String value) {

    private static final Set<String> PERMITIDAS = Set.of("CBA", "VG", "RDO");

    public MesaRegiao {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Mesa da região inválida");
        }
        value = value.trim().toUpperCase(Locale.ROOT);
        if (!PERMITIDAS.contains(value)) {
            throw new ValidationException("Mesa da região inválida");
        }
    }
}
