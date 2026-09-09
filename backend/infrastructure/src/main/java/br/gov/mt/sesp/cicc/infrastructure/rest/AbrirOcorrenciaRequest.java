package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.cad.AbrirOcorrenciaUseCase;

public record AbrirOcorrenciaRequest(
        String natureza,
        String descricao,
        String gravidade,
        String endereco,
        double latitude,
        double longitude,
        String protocolo,
        String telefone,
        String pabxUid,
        String pontoReferencia
) {

    AbrirOcorrenciaUseCase.Input toInput() {
        return new AbrirOcorrenciaUseCase.Input(
                natureza,
                descricao,
                gravidade,
                endereco,
                latitude,
                longitude,
                protocolo,
                telefone,
                pabxUid,
                pontoReferencia
        );
    }
}
