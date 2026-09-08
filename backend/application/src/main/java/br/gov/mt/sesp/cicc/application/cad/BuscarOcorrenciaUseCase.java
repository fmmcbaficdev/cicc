package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.application.UseCase;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaId;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaRepository;

import java.util.Objects;
import java.util.Optional;

public class BuscarOcorrenciaUseCase extends UseCase<BuscarOcorrenciaUseCase.Input, Optional<OcorrenciaVista>> {

    private final OcorrenciaRepository ocorrenciaRepository;

    public BuscarOcorrenciaUseCase(final OcorrenciaRepository ocorrenciaRepository) {
        this.ocorrenciaRepository = Objects.requireNonNull(ocorrenciaRepository);
    }

    @Override
    public Optional<OcorrenciaVista> execute(final Input input) {
        return ocorrenciaRepository.ocorrenciaDeId(OcorrenciaId.with(input.id()))
                .map(OcorrenciaVista::de);
    }

    public record Input(String id) {
    }
}
