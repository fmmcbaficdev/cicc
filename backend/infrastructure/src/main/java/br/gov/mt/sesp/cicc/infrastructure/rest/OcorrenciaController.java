package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.cad.AbrirOcorrenciaUseCase;
import br.gov.mt.sesp.cicc.application.cad.BuscarOcorrenciaUseCase;
import br.gov.mt.sesp.cicc.domain.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {

    private final AbrirOcorrenciaUseCase abrirOcorrenciaUseCase;
    private final BuscarOcorrenciaUseCase buscarOcorrenciaUseCase;

    public OcorrenciaController(
            final AbrirOcorrenciaUseCase abrirOcorrenciaUseCase,
            final BuscarOcorrenciaUseCase buscarOcorrenciaUseCase
    ) {
        this.abrirOcorrenciaUseCase = abrirOcorrenciaUseCase;
        this.buscarOcorrenciaUseCase = buscarOcorrenciaUseCase;
    }

    @PostMapping
    public ResponseEntity<OcorrenciaResponse> abrir(@RequestBody final AbrirOcorrenciaRequest request) {
        final var output = abrirOcorrenciaUseCase.execute(request.toInput());
        final var body = OcorrenciaResponse.from(output);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/ocorrencias/" + body.id()))
                .body(body);
    }

    @GetMapping("/{id}")
    public OcorrenciaResponse buscar(@PathVariable("id") final String id) {
        return buscarOcorrenciaUseCase.execute(new BuscarOcorrenciaUseCase.Input(id))
                .map(OcorrenciaResponse::from)
                .orElseThrow(() -> new NotFoundException("Ocorrência não encontrada"));
    }
}
