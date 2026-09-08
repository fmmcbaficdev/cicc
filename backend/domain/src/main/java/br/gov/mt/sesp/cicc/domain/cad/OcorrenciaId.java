package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

import java.util.UUID;

public record OcorrenciaId(String value) {

    public OcorrenciaId {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Identificador da ocorrência inválido");
        }
        value = value.trim();
    }

    public static OcorrenciaId unique() {
        return new OcorrenciaId(UUID.randomUUID().toString());
    }

    public static OcorrenciaId with(final String value) {
        return new OcorrenciaId(value);
    }
}
