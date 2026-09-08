package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.cad.EncaminharOcorrenciaUseCase;

public record EncaminharOcorrenciaRequest(String mesa) {

    EncaminharOcorrenciaUseCase.Input toInput(final String id) {
        return new EncaminharOcorrenciaUseCase.Input(id, mesa);
    }
}
