package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

public record Ponto(double latitude, double longitude) {

    public Ponto {
        if (Double.isNaN(latitude) || latitude < -90 || latitude > 90) {
            throw new ValidationException("Latitude inválida");
        }
        if (Double.isNaN(longitude) || longitude < -180 || longitude > 180) {
            throw new ValidationException("Longitude inválida");
        }
    }
}
