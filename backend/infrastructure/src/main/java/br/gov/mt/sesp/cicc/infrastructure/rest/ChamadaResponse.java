package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.pabx.ConsultarChamadaPabxUseCase;

import java.time.Instant;

public record ChamadaResponse(
        String uid,
        String telefone,
        String tronco,
        String unidade,
        Instant instante,
        Double latitude,
        Double longitude
) {

    public static ChamadaResponse from(final ConsultarChamadaPabxUseCase.Output output) {
        return new ChamadaResponse(
                output.uid(),
                output.telefone(),
                output.tronco(),
                output.unidade(),
                output.instante(),
                output.latitude(),
                output.longitude()
        );
    }
}
