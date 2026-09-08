package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

public record Ponto(double latitude, double longitude) {

    private static final double RAIO_TERRA_METROS = 6_371_000d;

    public Ponto {
        if (Double.isNaN(latitude) || latitude < -90 || latitude > 90) {
            throw new ValidationException("Latitude inválida");
        }
        if (Double.isNaN(longitude) || longitude < -180 || longitude > 180) {
            throw new ValidationException("Longitude inválida");
        }
    }

    public double distanciaEmMetros(final Ponto outro) {
        if (outro == null) {
            throw new ValidationException("Ponto de comparação é obrigatório");
        }
        final double lat1 = Math.toRadians(latitude);
        final double lat2 = Math.toRadians(outro.latitude);
        final double dLat = lat2 - lat1;
        final double dLon = Math.toRadians(outro.longitude - longitude);
        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * RAIO_TERRA_METROS * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
