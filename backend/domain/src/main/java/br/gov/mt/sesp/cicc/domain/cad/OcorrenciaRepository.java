package br.gov.mt.sesp.cicc.domain.cad;

import java.util.Optional;

public interface OcorrenciaRepository {

    Optional<Ocorrencia> ocorrenciaDeId(OcorrenciaId id);

    Optional<Ocorrencia> ocorrenciaDeProtocolo(Protocolo protocolo);

    Ocorrencia criar(Ocorrencia ocorrencia);

    Ocorrencia atualizar(Ocorrencia ocorrencia);
}
