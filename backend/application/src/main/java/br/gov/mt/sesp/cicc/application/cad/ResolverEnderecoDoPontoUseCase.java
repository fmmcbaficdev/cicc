package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.application.UseCase;
import br.gov.mt.sesp.cicc.domain.cad.GeocodificacaoPort;
import br.gov.mt.sesp.cicc.domain.cad.Ponto;

import java.util.Objects;
import java.util.Optional;

public class ResolverEnderecoDoPontoUseCase
        extends UseCase<ResolverEnderecoDoPontoUseCase.Input, Optional<ResolverEnderecoDoPontoUseCase.Output>> {

    private final GeocodificacaoPort geocodificacaoPort;

    public ResolverEnderecoDoPontoUseCase(final GeocodificacaoPort geocodificacaoPort) {
        this.geocodificacaoPort = Objects.requireNonNull(geocodificacaoPort);
    }

    @Override
    public Optional<Output> execute(final Input input) {
        return geocodificacaoPort.enderecoDe(new Ponto(input.latitude(), input.longitude())).map(Output::new);
    }

    public record Input(double latitude, double longitude) {
    }

    public record Output(String endereco) {
    }
}
