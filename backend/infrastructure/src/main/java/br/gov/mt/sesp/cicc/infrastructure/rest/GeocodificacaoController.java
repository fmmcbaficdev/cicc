package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.cad.ResolverEnderecoDoPontoUseCase;
import br.gov.mt.sesp.cicc.application.cad.ResolverPontoDoTextoUseCase;
import br.gov.mt.sesp.cicc.domain.exception.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geocodificacao")
public class GeocodificacaoController {

    private final ResolverEnderecoDoPontoUseCase resolverEnderecoDoPontoUseCase;
    private final ResolverPontoDoTextoUseCase resolverPontoDoTextoUseCase;

    public GeocodificacaoController(
            final ResolverEnderecoDoPontoUseCase resolverEnderecoDoPontoUseCase,
            final ResolverPontoDoTextoUseCase resolverPontoDoTextoUseCase
    ) {
        this.resolverEnderecoDoPontoUseCase = resolverEnderecoDoPontoUseCase;
        this.resolverPontoDoTextoUseCase = resolverPontoDoTextoUseCase;
    }

    @GetMapping("/endereco")
    public GeocodificacaoEnderecoResponse endereco(
            @RequestParam("latitude") final double latitude,
            @RequestParam("longitude") final double longitude
    ) {
        return resolverEnderecoDoPontoUseCase.execute(new ResolverEnderecoDoPontoUseCase.Input(latitude, longitude))
                .map(output -> new GeocodificacaoEnderecoResponse(output.endereco()))
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado para o ponto"));
    }

    @GetMapping("/ponto")
    public GeocodificacaoPontoResponse ponto(@RequestParam("texto") final String texto) {
        return resolverPontoDoTextoUseCase.execute(new ResolverPontoDoTextoUseCase.Input(texto))
                .map(output -> new GeocodificacaoPontoResponse(output.latitude(), output.longitude()))
                .orElseThrow(() -> new NotFoundException("Ponto de referência não encontrado"));
    }
}
