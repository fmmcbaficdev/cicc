package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

import java.util.Locale;
import java.util.Set;

public record Gravidade(String value) {

    private static final Set<String> PERMITIDAS = Set.of("BAIXA", "MEDIA", "ALTA", "CRITICA");

    public Gravidade {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Gravidade inválida");
        }
        value = value.trim().toUpperCase(Locale.ROOT);
        if (!PERMITIDAS.contains(value)) {
            throw new ValidationException("Gravidade inválida");
        }
    }

    public int ordemNaFila() {
        return switch (value) {
            case "CRITICA" -> 0;
            case "ALTA" -> 1;
            case "MEDIA" -> 2;
            default -> 3;
        };
    }
}
