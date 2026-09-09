package br.gov.mt.sesp.cicc.infrastructure.geo;

import br.gov.mt.sesp.cicc.domain.cad.GeocodificacaoPort;
import br.gov.mt.sesp.cicc.domain.cad.Ponto;

import java.util.Optional;

public class MockGeocodificacaoAdapter implements GeocodificacaoPort {

    public static final Ponto PONTO_CELULAR = new Ponto(-15.601411, -56.097892);
    public static final String ENDERECO_CELULAR = "Av. Historiador Rubens de Mendonça, Cuiabá";
    public static final Ponto PONTO_REFERENCIA = new Ponto(-15.6022, -56.0991);

    @Override
    public Optional<String> enderecoDe(final Ponto ponto) {
        if (ponto == null) {
            return Optional.empty();
        }
        if (PONTO_CELULAR.distanciaEmMetros(ponto) < 80) {
            return Optional.of(ENDERECO_CELULAR);
        }
        return Optional.of("Proximidade do celular, Cuiabá");
    }

    @Override
    public Optional<Ponto> pontoDe(final String texto) {
        if (texto == null || texto.isBlank()) {
            return Optional.empty();
        }
        final var normalizado = texto.toLowerCase();
        if (normalizado.contains("shopping") || normalizado.contains("praça") || normalizado.contains("praca")
                || normalizado.contains("posto") || normalizado.contains("referência")
                || normalizado.contains("referencia")) {
            return Optional.of(PONTO_REFERENCIA);
        }
        return Optional.empty();
    }
}
