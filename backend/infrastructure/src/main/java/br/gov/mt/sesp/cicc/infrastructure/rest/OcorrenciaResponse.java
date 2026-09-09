package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.cad.OcorrenciaVista;

import java.time.Instant;

public record OcorrenciaResponse(
        String id,
        String protocolo,
        String natureza,
        String gravidade,
        Instant inicioAtendimento,
        String telefone,
        String pabxUid,
        String endereco,
        String pontoReferencia,
        double latitude,
        double longitude,
        String situacao,
        String mesa,
        Instant encaminhadaEm,
        String prefixoEmpenhado,
        Instant inicioDeslocamento,
        Instant noLocalEm,
        boolean noLocalManual,
        Instant encerradaEm
) {

    public static OcorrenciaResponse from(final OcorrenciaVista output) {
        return new OcorrenciaResponse(
                output.id(),
                output.protocolo(),
                output.natureza(),
                output.gravidade(),
                output.inicioAtendimento(),
                output.telefone(),
                output.pabxUid(),
                output.endereco(),
                output.pontoReferencia(),
                output.latitude(),
                output.longitude(),
                output.situacao(),
                output.mesa(),
                output.encaminhadaEm(),
                output.prefixoEmpenhado(),
                output.inicioDeslocamento(),
                output.noLocalEm(),
                output.noLocalManual(),
                output.encerradaEm()
        );
    }
}
