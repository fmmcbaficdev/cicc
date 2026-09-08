package br.gov.mt.sesp.cicc.infrastructure.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiInfoController {

    @GetMapping("/")
    public Map<String, String> info() {
        return Map.of(
                "servico", "cicc-cad",
                "abrir", "POST /ocorrencias",
                "consultar", "GET /ocorrencias/{id}",
                "chamada", "GET /pabx/chamada",
                "mesa", "GET /mesa/{mesa}/ocorrencias",
                "empenhar", "POST /ocorrencias/{id}/empenhar",
                "exemplo", "docs/exemplo-abrir-ocorrencia.json"
        );
    }
}
