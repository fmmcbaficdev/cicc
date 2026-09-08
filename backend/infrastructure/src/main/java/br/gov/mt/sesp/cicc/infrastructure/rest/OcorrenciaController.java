package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.cad.AbrirOcorrenciaUseCase;
import br.gov.mt.sesp.cicc.application.cad.BuscarOcorrenciaUseCase;
import br.gov.mt.sesp.cicc.application.cad.EncaminharOcorrenciaUseCase;
import br.gov.mt.sesp.cicc.application.sala.EmpenharViaturaUseCase;
import br.gov.mt.sesp.cicc.application.sala.SugerirViaturasUseCase;
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
import java.util.List;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {

    private final AbrirOcorrenciaUseCase abrirOcorrenciaUseCase;
    private final BuscarOcorrenciaUseCase buscarOcorrenciaUseCase;
    private final EncaminharOcorrenciaUseCase encaminharOcorrenciaUseCase;
    private final SugerirViaturasUseCase sugerirViaturasUseCase;
    private final EmpenharViaturaUseCase empenharViaturaUseCase;

    public OcorrenciaController(
            final AbrirOcorrenciaUseCase abrirOcorrenciaUseCase,
            final BuscarOcorrenciaUseCase buscarOcorrenciaUseCase,
            final EncaminharOcorrenciaUseCase encaminharOcorrenciaUseCase,
            final SugerirViaturasUseCase sugerirViaturasUseCase,
            final EmpenharViaturaUseCase empenharViaturaUseCase
    ) {
        this.abrirOcorrenciaUseCase = abrirOcorrenciaUseCase;
        this.buscarOcorrenciaUseCase = buscarOcorrenciaUseCase;
        this.encaminharOcorrenciaUseCase = encaminharOcorrenciaUseCase;
        this.sugerirViaturasUseCase = sugerirViaturasUseCase;
        this.empenharViaturaUseCase = empenharViaturaUseCase;
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

    @PostMapping("/{id}/encaminhar")
    public OcorrenciaResponse encaminhar(
            @PathVariable("id") final String id,
            @RequestBody final EncaminharOcorrenciaRequest request
    ) {
        return OcorrenciaResponse.from(encaminharOcorrenciaUseCase.execute(request.toInput(id)));
    }

    @GetMapping("/{id}/viaturas-sugeridas")
    public List<ViaturaSugeridaResponse> sugerir(@PathVariable("id") final String id) {
        return sugerirViaturasUseCase.execute(new SugerirViaturasUseCase.Input(id)).stream()
                .map(ViaturaSugeridaResponse::from)
                .toList();
    }

    @PostMapping("/{id}/empenhar")
    public OcorrenciaResponse empenhar(
            @PathVariable("id") final String id,
            @RequestBody final EmpenharViaturaRequest request
    ) {
        return OcorrenciaResponse.from(empenharViaturaUseCase.execute(request.toInput(id)));
    }
}
