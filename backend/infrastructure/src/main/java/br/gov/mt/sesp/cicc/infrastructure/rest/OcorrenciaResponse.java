package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.cad.AbrirOcorrenciaUseCase;
import br.gov.mt.sesp.cicc.application.cad.BuscarOcorrenciaUseCase;

import java.time.Instant;

public record OcorrenciaResponse(
        String id,
        String protocolo,
        Instant inicioAtendimento,
        String telefone,
        String pabxUid
) {

    public static OcorrenciaResponse from(final AbrirOcorrenciaUseCase.Output output) {
        return new OcorrenciaResponse(
                output.id(),
                output.protocolo(),
                output.inicioAtendimento(),
                output.telefone(),
                output.pabxUid()
        );
    }

    public static OcorrenciaResponse from(final BuscarOcorrenciaUseCase.Output output) {
        return new OcorrenciaResponse(
                output.id(),
                output.protocolo(),
                output.inicioAtendimento(),
                output.telefone(),
                output.pabxUid()
        );
    }
}
