package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

public record Protocolo(String value) {

    public Protocolo {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Protocolo inválido");
        }
        value = value.trim();
        if (value.length() < 3 || value.length() > 40 || !value.matches("[A-Za-z0-9./-]+")) {
            throw new ValidationException("Protocolo inválido");
        }
    }
}
