package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.cad.OcorrenciaVista;

import java.time.Instant;

public record OcorrenciaResponse(
        String id,
        String protocolo,
        Instant inicioAtendimento,
        String telefone,
        String pabxUid,
        String endereco,
        double latitude,
        double longitude,
        String situacao,
        String mesa,
        Instant encaminhadaEm,
        String prefixoEmpenhado,
        Instant inicioDeslocamento
) {

    public static OcorrenciaResponse from(final OcorrenciaVista output) {
        return new OcorrenciaResponse(
                output.id(),
                output.protocolo(),
                output.inicioAtendimento(),
                output.telefone(),
                output.pabxUid(),
                output.endereco(),
                output.latitude(),
                output.longitude(),
                output.situacao(),
                output.mesa(),
                output.encaminhadaEm(),
                output.prefixoEmpenhado(),
                output.inicioDeslocamento()
        );
    }
}
