package br.gov.mt.sesp.cicc.application.sala;

import br.gov.mt.sesp.cicc.application.UseCase;
import br.gov.mt.sesp.cicc.application.cad.OcorrenciaVista;
import br.gov.mt.sesp.cicc.domain.avl.AvlPort;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaId;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.exception.NotFoundException;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public class EncerrarOcorrenciaUseCase extends UseCase<EncerrarOcorrenciaUseCase.Input, OcorrenciaVista> {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final AvlPort avlPort;
    private final Clock clock;

    public EncerrarOcorrenciaUseCase(final OcorrenciaRepository ocorrenciaRepository, final AvlPort avlPort) {
        this(ocorrenciaRepository, avlPort, Clock.systemUTC());
    }

    public EncerrarOcorrenciaUseCase(
            final OcorrenciaRepository ocorrenciaRepository,
            final AvlPort avlPort,
            final Clock clock
    ) {
        this.ocorrenciaRepository = Objects.requireNonNull(ocorrenciaRepository);
        this.avlPort = Objects.requireNonNull(avlPort);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public OcorrenciaVista execute(final Input input) {
        final var ocorrencia = ocorrenciaRepository.ocorrenciaDeId(OcorrenciaId.with(input.ocorrenciaId()))
                .orElseThrow(() -> new NotFoundException("Ocorrência não encontrada"));
        ocorrencia.encerrar(Instant.now(clock));
        avlPort.liberar(ocorrencia.prefixoEmpenhado());
        return OcorrenciaVista.de(ocorrenciaRepository.atualizar(ocorrencia));
    }

    public record Input(String ocorrenciaId) {
    }
}
