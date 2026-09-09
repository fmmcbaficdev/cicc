package br.gov.mt.sesp.cicc.application.cad;

import br.gov.mt.sesp.cicc.application.UseCase;
import br.gov.mt.sesp.cicc.domain.cad.Ocorrencia;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.cad.Protocolo;
import br.gov.mt.sesp.cicc.domain.cad.Telefone;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import br.gov.mt.sesp.cicc.domain.pabx.PabxPort;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

public class AbrirOcorrenciaUseCase extends UseCase<AbrirOcorrenciaUseCase.Input, OcorrenciaVista> {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final PabxPort pabxPort;
    private final Clock clock;

    public AbrirOcorrenciaUseCase(final OcorrenciaRepository ocorrenciaRepository, final PabxPort pabxPort) {
        this(ocorrenciaRepository, pabxPort, Clock.systemUTC());
    }

    public AbrirOcorrenciaUseCase(
            final OcorrenciaRepository ocorrenciaRepository,
            final PabxPort pabxPort,
            final Clock clock
    ) {
        this.ocorrenciaRepository = Objects.requireNonNull(ocorrenciaRepository);
        this.pabxPort = Objects.requireNonNull(pabxPort);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public OcorrenciaVista execute(final Input input) {
        final var protocolo = new Protocolo(input.protocolo());
        ocorrenciaRepository.ocorrenciaDeProtocolo(protocolo).ifPresent(existente -> {
            throw new ValidationException("Já existe ocorrência com este protocolo");
        });

        final var correlacao = correlacionar(input);
        final var ocorrencia = ocorrenciaRepository.criar(Ocorrencia.newOcorrencia(
                input.natureza(),
                input.descricao(),
                input.gravidade(),
                input.endereco(),
                input.latitude(),
                input.longitude(),
                input.protocolo(),
                Instant.now(clock),
                correlacao.telefone(),
                correlacao.pabxUid(),
                input.pontoReferencia()
        ));

        return OcorrenciaVista.de(ocorrencia);
    }

    private Correlacao correlacionar(final Input input) {
        if (input.pabxUid() != null && !input.pabxUid().isBlank()) {
            final var chamada = pabxPort.chamadaDeUid(input.pabxUid());
            if (chamada.isPresent()) {
                return new Correlacao(chamada.get().telefone(), chamada.get().uid());
            }
        }
        if (input.telefone() != null && !input.telefone().isBlank()) {
            return new Correlacao(new Telefone(input.telefone()), null);
        }
        return new Correlacao(null, null);
    }

    private record Correlacao(Telefone telefone, String pabxUid) {
    }

    public record Input(
            String natureza,
            String descricao,
            String gravidade,
            String endereco,
            double latitude,
            double longitude,
            String protocolo,
            String telefone,
            String pabxUid,
            String pontoReferencia
    ) {
        public Input(
                final String natureza,
                final String descricao,
                final String gravidade,
                final String endereco,
                final double latitude,
                final double longitude,
                final String protocolo
        ) {
            this(natureza, descricao, gravidade, endereco, latitude, longitude, protocolo, null, null, null);
        }

        public Input(
                final String natureza,
                final String descricao,
                final String gravidade,
                final String endereco,
                final double latitude,
                final double longitude,
                final String protocolo,
                final String telefone,
                final String pabxUid
        ) {
            this(natureza, descricao, gravidade, endereco, latitude, longitude, protocolo, telefone, pabxUid, null);
        }
    }

}
