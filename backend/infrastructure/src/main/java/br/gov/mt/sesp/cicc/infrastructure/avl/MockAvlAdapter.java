package br.gov.mt.sesp.cicc.infrastructure.avl;

import br.gov.mt.sesp.cicc.domain.avl.AvlPort;
import br.gov.mt.sesp.cicc.domain.avl.Recurso;
import br.gov.mt.sesp.cicc.domain.cad.Ponto;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MockAvlAdapter implements AvlPort {

    public static final String PREFIXO_MAIS_PROXIMA = "PM-CBA-01";

    private final ConcurrentMap<String, Recurso> frota = new ConcurrentHashMap<>();

    public MockAvlAdapter() {
        registrar(PREFIXO_MAIS_PROXIMA, -15.6018, -56.0982, "LIVRE");
        registrar("PM-CBA-02", -15.6080, -56.1050, "LIVRE");
        registrar("PM-CBA-03", -15.5700, -56.0700, "LIVRE");
        registrar("PM-CBA-99", -15.6015, -56.0975, "EMPENHADA");
    }

    private void registrar(final String prefixo, final double latitude, final double longitude, final String situacao) {
        frota.put(prefixo, new Recurso(prefixo, new Ponto(latitude, longitude), situacao));
    }

    @Override
    public List<Recurso> viaturasLivres() {
        return frota.values().stream().filter(Recurso::livre).toList();
    }

    @Override
    public Optional<Recurso> viaturaDePrefixo(final String prefixo) {
        if (prefixo == null || prefixo.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(frota.get(prefixo.trim()));
    }

    @Override
    public void empenhar(final String prefixo) {
        final var atual = viaturaDePrefixo(prefixo)
                .orElseThrow(() -> new ValidationException("Viatura não encontrada no AVL"));
        if (!atual.livre()) {
            throw new ValidationException("Viatura não está Livre");
        }
        frota.put(atual.prefixo(), new Recurso(atual.prefixo(), atual.ponto(), "EMPENHADA"));
    }
}
