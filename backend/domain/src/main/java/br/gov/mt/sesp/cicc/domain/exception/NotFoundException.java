package br.gov.mt.sesp.cicc.domain.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(final String mensagem) {
        super(mensagem);
    }
}
