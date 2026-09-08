package br.gov.mt.sesp.cicc.application.pabx;

import br.gov.mt.sesp.cicc.application.NullaryUseCase;
import br.gov.mt.sesp.cicc.domain.pabx.PabxPort;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class ConsultarChamadaPabxUseCase extends NullaryUseCase<Optional<ConsultarChamadaPabxUseCase.Output>> {

    private final PabxPort pabxPort;

    public ConsultarChamadaPabxUseCase(final PabxPort pabxPort) {
        this.pabxPort = Objects.requireNonNull(pabxPort);
    }

    @Override
    public Optional<Output> execute() {
        return pabxPort.chamadaAtual()
                .map(chamada -> new Output(
                        chamada.uid(),
                        chamada.telefone().value(),
                        chamada.tronco(),
                        chamada.unidade(),
                        chamada.instante()
                ));
    }

    public record Output(String uid, String telefone, String tronco, String unidade, Instant instante) {
    }
}
