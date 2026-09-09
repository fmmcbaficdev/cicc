package br.gov.mt.sesp.cicc.domain.cad;

import java.util.Optional;

public interface GeocodificacaoPort {

    Optional<String> enderecoDe(Ponto ponto);

    Optional<Ponto> pontoDe(String texto);
}
