package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.sala.SugerirViaturasUseCase;

public record ViaturaSugeridaResponse(
        String prefixo,
        double latitude,
        double longitude,
        int distanciaMetros
) {

    public static ViaturaSugeridaResponse from(final SugerirViaturasUseCase.Sugestao sugestao) {
        return new ViaturaSugeridaResponse(
                sugestao.prefixo(),
                sugestao.latitude(),
                sugestao.longitude(),
                sugestao.distanciaMetros()
        );
    }
}
