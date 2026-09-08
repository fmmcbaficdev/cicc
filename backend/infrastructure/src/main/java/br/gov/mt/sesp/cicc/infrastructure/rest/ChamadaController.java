package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.application.pabx.ConsultarChamadaPabxUseCase;
import br.gov.mt.sesp.cicc.domain.exception.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pabx/chamada")
public class ChamadaController {

    private final ConsultarChamadaPabxUseCase consultarChamadaPabxUseCase;

    public ChamadaController(final ConsultarChamadaPabxUseCase consultarChamadaPabxUseCase) {
        this.consultarChamadaPabxUseCase = consultarChamadaPabxUseCase;
    }

    @GetMapping
    public ChamadaResponse atual() {
        return consultarChamadaPabxUseCase.execute()
                .map(ChamadaResponse::from)
                .orElseThrow(() -> new NotFoundException("Ligação não encontrada"));
    }
}
