package br.gov.mt.sesp.cicc.domain.cad;

import java.util.List;
import java.util.Optional;

public interface OcorrenciaRepository {

    Optional<Ocorrencia> ocorrenciaDeId(OcorrenciaId id);

    Optional<Ocorrencia> ocorrenciaDeProtocolo(Protocolo protocolo);

    List<Ocorrencia> ocorrenciasNaMesa(MesaRegiao mesa);

    Ocorrencia criar(Ocorrencia ocorrencia);

    Ocorrencia atualizar(Ocorrencia ocorrencia);
}
