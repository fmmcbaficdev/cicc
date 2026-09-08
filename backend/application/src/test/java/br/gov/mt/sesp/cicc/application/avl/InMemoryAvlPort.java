package br.gov.mt.sesp.cicc.application.avl;

import br.gov.mt.sesp.cicc.domain.avl.AvlPort;
import br.gov.mt.sesp.cicc.domain.avl.Recurso;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryAvlPort implements AvlPort {

    private final Map<String, Recurso> porPrefixo = new LinkedHashMap<>();

    public void registrar(final Recurso recurso) {
        porPrefixo.put(recurso.prefixo(), recurso);
    }

    @Override
    public List<Recurso> viaturasLivres() {
        return porPrefixo.values().stream().filter(Recurso::livre).toList();
    }

    @Override
    public Optional<Recurso> viaturaDePrefixo(final String prefixo) {
        if (prefixo == null || prefixo.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(porPrefixo.get(prefixo.trim()));
    }

    @Override
    public void empenhar(final String prefixo) {
        final var atual = viaturaDePrefixo(prefixo)
                .orElseThrow(() -> new ValidationException("Viatura não encontrada no AVL"));
        if (!atual.livre()) {
            throw new ValidationException("Viatura não está Livre");
        }
        porPrefixo.put(atual.prefixo(), new Recurso(atual.prefixo(), atual.ponto(), "EMPENHADA"));
    }
}
