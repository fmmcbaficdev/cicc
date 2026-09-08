package br.gov.mt.sesp.cicc.domain.sala;

import br.gov.mt.sesp.cicc.domain.cad.Ponto;

public record SugestaoViatura(String prefixo, Ponto ponto, int distanciaMetros) {
}
