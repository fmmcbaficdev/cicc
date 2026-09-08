package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.sala.EmpenharViaturaUseCase;

public record EmpenharViaturaRequest(String prefixo) {

    public EmpenharViaturaUseCase.Input toInput(final String ocorrenciaId) {
        return new EmpenharViaturaUseCase.Input(ocorrenciaId, prefixo);
    }
}
