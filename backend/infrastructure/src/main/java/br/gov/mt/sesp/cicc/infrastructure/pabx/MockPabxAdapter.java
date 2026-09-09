package br.gov.mt.sesp.cicc.infrastructure.pabx;

import br.gov.mt.sesp.cicc.domain.cad.Ponto;
import br.gov.mt.sesp.cicc.domain.cad.Telefone;
import br.gov.mt.sesp.cicc.domain.pabx.Chamada;
import br.gov.mt.sesp.cicc.domain.pabx.PabxPort;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

public class MockPabxAdapter implements PabxPort {

    public static final String UID_AMOSTRA = "pabx-mock-190-cba-001";
    public static final String TELEFONE_AMOSTRA = "65981234567";
    public static final String TRONCO_AMOSTRA = "190";
    public static final String UNIDADE_AMOSTRA = "CBA";
    public static final Ponto PONTO_CELULAR_AMOSTRA = new Ponto(-15.601411, -56.097892);

    private final boolean mudo;
    private final Chamada amostra;

    public MockPabxAdapter(final boolean mudo, final Clock clock) {
        this.mudo = mudo;
        this.amostra = new Chamada(
                UID_AMOSTRA,
                new Telefone(TELEFONE_AMOSTRA),
                TRONCO_AMOSTRA,
                UNIDADE_AMOSTRA,
                Instant.now(clock),
                PONTO_CELULAR_AMOSTRA
        );
    }

    @Override
    public Optional<Chamada> chamadaAtual() {
        return mudo ? Optional.empty() : Optional.of(amostra);
    }

    @Override
    public Optional<Chamada> chamadaDeUid(final String uid) {
        if (mudo || uid == null || !UID_AMOSTRA.equals(uid.trim())) {
            return Optional.empty();
        }
        return Optional.of(amostra);
    }
}
