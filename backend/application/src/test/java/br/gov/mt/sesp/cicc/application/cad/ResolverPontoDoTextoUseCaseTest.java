package br.gov.mt.sesp.cicc.application.cad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResolverPontoDoTextoUseCaseTest {

    @Test
    @DisplayName("acha o ponto de um marco conhecido")
    void resolvePontoDeReferencia() {
        final var useCase = new ResolverPontoDoTextoUseCase(new InMemoryGeocodificacaoPort());

        final var output = useCase.execute(new ResolverPontoDoTextoUseCase.Input("Shopping Estação"));

        assertTrue(output.isPresent());
        assertEquals(InMemoryGeocodificacaoPort.PONTO_REFERENCIA.latitude(), output.get().latitude());
        assertEquals(InMemoryGeocodificacaoPort.PONTO_REFERENCIA.longitude(), output.get().longitude());
    }

    @Test
    @DisplayName("texto desconhecido não inventa ponto")
    void textoDesconhecidoFicaVazio() {
        final var useCase = new ResolverPontoDoTextoUseCase(new InMemoryGeocodificacaoPort());

        assertTrue(useCase.execute(new ResolverPontoDoTextoUseCase.Input("rua sem nome")).isEmpty());
    }
}
