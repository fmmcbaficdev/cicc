package br.gov.mt.sesp.cicc.application.sala;

import br.gov.mt.sesp.cicc.application.UseCase;
import br.gov.mt.sesp.cicc.application.cad.OcorrenciaVista;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaId;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.exception.NotFoundException;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public class RegistrarNoLocalUseCase extends UseCase<RegistrarNoLocalUseCase.Input, OcorrenciaVista> {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final Clock clock;

    public RegistrarNoLocalUseCase(final OcorrenciaRepository ocorrenciaRepository) {
        this(ocorrenciaRepository, Clock.systemUTC());
    }

    public RegistrarNoLocalUseCase(final OcorrenciaRepository ocorrenciaRepository, final Clock clock) {
        this.ocorrenciaRepository = Objects.requireNonNull(ocorrenciaRepository);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public OcorrenciaVista execute(final Input input) {
        final var ocorrencia = ocorrenciaRepository.ocorrenciaDeId(OcorrenciaId.with(input.ocorrenciaId()))
                .orElseThrow(() -> new NotFoundException("Ocorrência não encontrada"));
        ocorrencia.registrarNoLocal(Instant.now(clock), true);
        return OcorrenciaVista.de(ocorrenciaRepository.atualizar(ocorrencia));
    }

    public record Input(String ocorrenciaId) {
    }
}
