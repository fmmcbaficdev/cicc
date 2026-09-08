package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.sala.ListarOcorrenciasNaMesaUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mesa")
public class MesaController {

    private final ListarOcorrenciasNaMesaUseCase listarOcorrenciasNaMesaUseCase;

    public MesaController(final ListarOcorrenciasNaMesaUseCase listarOcorrenciasNaMesaUseCase) {
        this.listarOcorrenciasNaMesaUseCase = listarOcorrenciasNaMesaUseCase;
    }

    @GetMapping("/{mesa}/ocorrencias")
    public List<OcorrenciaResponse> listar(@PathVariable("mesa") final String mesa) {
        return listarOcorrenciasNaMesaUseCase.execute(new ListarOcorrenciasNaMesaUseCase.Input(mesa)).stream()
                .map(OcorrenciaResponse::from)
                .toList();
    }
}
