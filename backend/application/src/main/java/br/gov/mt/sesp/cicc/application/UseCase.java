package br.gov.mt.sesp.cicc.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN input);
}
