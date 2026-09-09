package br.gov.mt.sesp.cicc.application.cad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResolverEnderecoDoPontoUseCaseTest {

    @Test
    @DisplayName("escreve o endereço do ponto do celular")
    void resolveEnderecoDoCelular() {
        final var useCase = new ResolverEnderecoDoPontoUseCase(new InMemoryGeocodificacaoPort());

        final var output = useCase.execute(new ResolverEnderecoDoPontoUseCase.Input(-15.601411, -56.097892));

        assertTrue(output.isPresent());
        assertEquals(InMemoryGeocodificacaoPort.ENDERECO_CELULAR, output.get().endereco());
    }
}
