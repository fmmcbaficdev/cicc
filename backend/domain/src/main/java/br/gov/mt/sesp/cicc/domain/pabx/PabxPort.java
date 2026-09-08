package br.gov.mt.sesp.cicc.domain.pabx;

import java.util.Optional;

public interface PabxPort {

    Optional<Chamada> chamadaAtual();

    Optional<Chamada> chamadaDeUid(String uid);
}
