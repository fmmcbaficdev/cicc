package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

public record Telefone(String value) {

    public Telefone {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Telefone inválido");
        }
        final var digits = value.replaceAll("\\D", "");
        if (digits.length() < 10 || digits.length() > 11) {
            throw new ValidationException("Telefone inválido");
        }
        value = digits;
    }
}
