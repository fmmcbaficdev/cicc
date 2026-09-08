package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaId;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.cad.Protocolo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryOcorrenciaRepository implements OcorrenciaRepository {

    private final Map<String, Ocorrencia> porId = new HashMap<>();
    private final Map<String, Ocorrencia> porProtocolo = new HashMap<>();

    @Override
    public Optional<Ocorrencia> ocorrenciaDeId(final OcorrenciaId id) {
        return Optional.ofNullable(porId.get(Objects.requireNonNull(id).value()));
    }

    @Override
    public Optional<Ocorrencia> ocorrenciaDeProtocolo(final Protocolo protocolo) {
        return Optional.ofNullable(porProtocolo.get(Objects.requireNonNull(protocolo).value()));
    }

    @Override
    public Ocorrencia criar(final Ocorrencia ocorrencia) {
        return salvar(ocorrencia);
    }

    @Override
    public Ocorrencia atualizar(final Ocorrencia ocorrencia) {
        return salvar(ocorrencia);
    }

    private Ocorrencia salvar(final Ocorrencia ocorrencia) {
        porId.put(ocorrencia.ocorrenciaId().value(), ocorrencia);
        porProtocolo.put(ocorrencia.protocolo().value(), ocorrencia);
        return ocorrencia;
    }
}
