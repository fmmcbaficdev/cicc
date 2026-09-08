package br.gov.mt.sesp.cicc.application.pabx;

import br.gov.mt.sesp.cicc.domain.pabx.Chamada;
import br.gov.mt.sesp.cicc.domain.pabx.PabxPort;

import java.util.Optional;

public class InMemoryPabxPort implements PabxPort {

    private Chamada atual;

    public void definirChamadaAtual(final Chamada chamada) {
        this.atual = chamada;
    }

    public void silenciar() {
        this.atual = null;
    }

    @Override
    public Optional<Chamada> chamadaAtual() {
        return Optional.ofNullable(atual);
    }

    @Override
    public Optional<Chamada> chamadaDeUid(final String uid) {
        if (atual == null || uid == null || uid.isBlank()) {
            return Optional.empty();
        }
        return atual.uid().equals(uid.trim()) ? Optional.of(atual) : Optional.empty();
    }
}
