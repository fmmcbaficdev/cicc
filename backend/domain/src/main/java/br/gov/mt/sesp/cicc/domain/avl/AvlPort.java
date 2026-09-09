package br.gov.mt.sesp.cicc.domain.avl;

import java.util.List;
import java.util.Optional;

public interface AvlPort {

    List<Recurso> viaturasLivres();

    Optional<Recurso> viaturaDePrefixo(String prefixo);

    void empenhar(String prefixo);

    void liberar(String prefixo);
}
