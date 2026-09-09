package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

public record Natureza(String value) {

    private static final int TAMANHO_MAXIMO = 80;

    public Natureza {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Natureza da ocorrência é obrigatória");
        }
        value = value.trim();
        if (value.length() > TAMANHO_MAXIMO) {
            throw new ValidationException("Natureza da ocorrência deve ter no máximo 80 caracteres");
        }
    }
}
