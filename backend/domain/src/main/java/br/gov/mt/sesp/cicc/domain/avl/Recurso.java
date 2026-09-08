package br.gov.mt.sesp.cicc.domain.avl;

import br.gov.mt.sesp.cicc.domain.cad.Ponto;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

import java.util.Set;

public record Recurso(String prefixo, Ponto ponto, String situacao) {

    public Recurso {
        if (prefixo == null || prefixo.isBlank()) {
            throw new ValidationException("Prefixo da viatura é obrigatório");
        }
        if (ponto == null) {
            throw new ValidationException("Ponto da viatura é obrigatório");
        }
        if (situacao == null || !Set.of("LIVRE", "EMPENHADA").contains(situacao)) {
            throw new ValidationException("Situação da viatura inválida");
        }
        prefixo = prefixo.trim();
    }

    public boolean livre() {
        return "LIVRE".equals(situacao);
    }
}
