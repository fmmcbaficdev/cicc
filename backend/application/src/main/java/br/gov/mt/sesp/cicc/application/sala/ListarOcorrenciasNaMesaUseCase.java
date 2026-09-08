package br.gov.mt.sesp.cicc.application.sala;

import br.gov.mt.sesp.cicc.application.UseCase;
import br.gov.mt.sesp.cicc.application.cad.OcorrenciaVista;
import br.gov.mt.sesp.cicc.domain.cad.MesaRegiao;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.sala.FilaMesa;

import java.util.List;
import java.util.Objects;

public class ListarOcorrenciasNaMesaUseCase
        extends UseCase<ListarOcorrenciasNaMesaUseCase.Input, List<OcorrenciaVista>> {

    private final OcorrenciaRepository ocorrenciaRepository;

    public ListarOcorrenciasNaMesaUseCase(final OcorrenciaRepository ocorrenciaRepository) {
        this.ocorrenciaRepository = Objects.requireNonNull(ocorrenciaRepository);
    }

    @Override
    public List<OcorrenciaVista> execute(final Input input) {
        return FilaMesa.ordenar(ocorrenciaRepository.ocorrenciasNaMesa(new MesaRegiao(input.mesa()))).stream()
                .map(OcorrenciaVista::de)
                .toList();
    }

    public record Input(String mesa) {
    }
}
