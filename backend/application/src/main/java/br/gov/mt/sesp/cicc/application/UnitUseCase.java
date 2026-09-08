package br.gov.mt.sesp.cicc.application;

public abstract class UnitUseCase<IN> {

    public abstract void execute(IN input);
}
