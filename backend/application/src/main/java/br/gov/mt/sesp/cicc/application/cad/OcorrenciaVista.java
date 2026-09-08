package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;

import java.time.Instant;

public record OcorrenciaVista(
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
        Instant encaminhadaEm
) {

    public static OcorrenciaVista de(final Ocorrencia ocorrencia) {
        return new OcorrenciaVista(
                ocorrencia.ocorrenciaId().value(),
                ocorrencia.protocolo().value(),
                ocorrencia.inicioAtendimento(),
                ocorrencia.telefone() == null ? null : ocorrencia.telefone().value(),
                ocorrencia.pabxUid(),
                ocorrencia.endereco(),
                ocorrencia.ponto().latitude(),
                ocorrencia.ponto().longitude(),
                ocorrencia.situacao(),
                ocorrencia.mesa() == null ? null : ocorrencia.mesa().value(),
                ocorrencia.encaminhadaEm()
        );
    }
}
