package br.gov.mt.sesp.cicc.application.sala;

import br.gov.mt.sesp.cicc.application.UseCase;
import br.gov.mt.sesp.cicc.domain.avl.AvlPort;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaId;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.exception.NotFoundException;
import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import br.gov.mt.sesp.cicc.domain.sala.MatchingProximidade;
import br.gov.mt.sesp.cicc.domain.sala.SugestaoViatura;

import java.util.List;
import java.util.Objects;

public class SugerirViaturasUseCase extends UseCase<SugerirViaturasUseCase.Input, List<SugerirViaturasUseCase.Sugestao>> {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final AvlPort avlPort;

    public SugerirViaturasUseCase(final OcorrenciaRepository ocorrenciaRepository, final AvlPort avlPort) {
        this.ocorrenciaRepository = Objects.requireNonNull(ocorrenciaRepository);
        this.avlPort = Objects.requireNonNull(avlPort);
    }

    @Override
    public List<Sugestao> execute(final Input input) {
        final var ocorrencia = ocorrenciaRepository.ocorrenciaDeId(OcorrenciaId.with(input.ocorrenciaId()))
                .orElseThrow(() -> new NotFoundException("Ocorrência não encontrada"));
        if (ocorrencia.encaminhadaEm() == null) {
            throw new ValidationException("Ocorrência ainda não está na mesa do despachador");
        }
        if (ocorrencia.inicioDeslocamento() != null) {
            return List.of();
        }
        final var livres = avlPort.viaturasLivres();
        final var sugestoes = input.ampliar()
                ? MatchingProximidade.ampliadas(ocorrencia.ponto(), livres)
                : MatchingProximidade.maisProximas(ocorrencia.ponto(), livres);
        return sugestoes.stream()
                .map(Sugestao::de)
                .toList();
    }

    public record Input(String ocorrenciaId, boolean ampliar) {

        public Input(final String ocorrenciaId) {
            this(ocorrenciaId, false);
        }
    }

    public record Sugestao(String prefixo, double latitude, double longitude, int distanciaMetros) {

        static Sugestao de(final SugestaoViatura sugestao) {
            return new Sugestao(
                    sugestao.prefixo(),
                    sugestao.ponto().latitude(),
                    sugestao.ponto().longitude(),
                    sugestao.distanciaMetros()
            );
        }
    }
}
