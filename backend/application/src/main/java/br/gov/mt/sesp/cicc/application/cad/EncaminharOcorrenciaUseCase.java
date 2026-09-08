package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.application.UseCase;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaId;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.exception.NotFoundException;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public class EncaminharOcorrenciaUseCase extends UseCase<EncaminharOcorrenciaUseCase.Input, OcorrenciaVista> {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final Clock clock;

    public EncaminharOcorrenciaUseCase(final OcorrenciaRepository ocorrenciaRepository) {
        this(ocorrenciaRepository, Clock.systemUTC());
    }

    public EncaminharOcorrenciaUseCase(final OcorrenciaRepository ocorrenciaRepository, final Clock clock) {
        this.ocorrenciaRepository = Objects.requireNonNull(ocorrenciaRepository);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public OcorrenciaVista execute(final Input input) {
        final var ocorrencia = ocorrenciaRepository.ocorrenciaDeId(OcorrenciaId.with(input.id()))
                .orElseThrow(() -> new NotFoundException("Ocorrência não encontrada"));
        ocorrencia.encaminharAMesa(input.mesa(), Instant.now(clock));
        return OcorrenciaVista.de(ocorrenciaRepository.atualizar(ocorrencia));
    }

    public record Input(String id, String mesa) {
    }
}
