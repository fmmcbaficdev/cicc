package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.application.UseCase;
import br.gov.mt.sesp.cicc.domain.cad.GeocodificacaoPort;

import java.util.Objects;
import java.util.Optional;

public class ResolverPontoDoTextoUseCase
        extends UseCase<ResolverPontoDoTextoUseCase.Input, Optional<ResolverPontoDoTextoUseCase.Output>> {

    private final GeocodificacaoPort geocodificacaoPort;

    public ResolverPontoDoTextoUseCase(final GeocodificacaoPort geocodificacaoPort) {
        this.geocodificacaoPort = Objects.requireNonNull(geocodificacaoPort);
    }

    @Override
    public Optional<Output> execute(final Input input) {
        return geocodificacaoPort.pontoDe(input.texto())
                .map(ponto -> new Output(ponto.latitude(), ponto.longitude()));
    }

    public record Input(String texto) {
    }

    public record Output(double latitude, double longitude) {
    }
}
