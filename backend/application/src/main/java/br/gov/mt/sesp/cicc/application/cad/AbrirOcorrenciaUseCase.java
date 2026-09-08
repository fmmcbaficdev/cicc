package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.application.UseCase;
import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.cad.Protocolo;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public class AbrirOcorrenciaUseCase extends UseCase<AbrirOcorrenciaUseCase.Input, AbrirOcorrenciaUseCase.Output> {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final Clock clock;

    public AbrirOcorrenciaUseCase(final OcorrenciaRepository ocorrenciaRepository) {
        this(ocorrenciaRepository, Clock.systemUTC());
    }

    public AbrirOcorrenciaUseCase(final OcorrenciaRepository ocorrenciaRepository, final Clock clock) {
        this.ocorrenciaRepository = Objects.requireNonNull(ocorrenciaRepository);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public Output execute(final Input input) {
        final var protocolo = new Protocolo(input.protocolo());
        ocorrenciaRepository.ocorrenciaDeProtocolo(protocolo).ifPresent(existente -> {
            throw new ValidationException("Já existe ocorrência com este protocolo");
        });

        final var ocorrencia = ocorrenciaRepository.criar(Ocorrencia.newOcorrencia(
                input.descricao(),
                input.gravidade(),
                input.endereco(),
                input.latitude(),
                input.longitude(),
                input.protocolo(),
                Instant.now(clock)
        ));

        return new Output(
                ocorrencia.ocorrenciaId().value(),
                ocorrencia.protocolo().value(),
                ocorrencia.inicioAtendimento()
        );
    }

    public record Input(
            String descricao,
            String gravidade,
            String endereco,
            double latitude,
            double longitude,
            String protocolo
    ) {
    }

    public record Output(String id, String protocolo, Instant inicioAtendimento) {
    }
}
