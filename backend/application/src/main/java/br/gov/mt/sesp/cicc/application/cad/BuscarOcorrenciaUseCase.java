package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.application.UseCase;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaId;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaRepository;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class BuscarOcorrenciaUseCase extends UseCase<BuscarOcorrenciaUseCase.Input, Optional<BuscarOcorrenciaUseCase.Output>> {

    private final OcorrenciaRepository ocorrenciaRepository;

    public BuscarOcorrenciaUseCase(final OcorrenciaRepository ocorrenciaRepository) {
        this.ocorrenciaRepository = Objects.requireNonNull(ocorrenciaRepository);
    }

    @Override
    public Optional<Output> execute(final Input input) {
        return ocorrenciaRepository.ocorrenciaDeId(OcorrenciaId.with(input.id()))
                .map(ocorrencia -> new Output(
                        ocorrencia.ocorrenciaId().value(),
                        ocorrencia.protocolo().value(),
                        ocorrencia.inicioAtendimento(),
                        ocorrencia.telefone() == null ? null : ocorrencia.telefone().value(),
                        ocorrencia.pabxUid()
                ));
    }

    public record Input(String id) {
    }

    public record Output(String id, String protocolo, Instant inicioAtendimento, String telefone, String pabxUid) {
    }
}
