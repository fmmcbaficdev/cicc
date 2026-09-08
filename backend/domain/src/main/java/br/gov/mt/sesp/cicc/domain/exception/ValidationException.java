package br.gov.mt.sesp.cicc.domain.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(final String mensagem) {
        super(mensagem);
    }
}
