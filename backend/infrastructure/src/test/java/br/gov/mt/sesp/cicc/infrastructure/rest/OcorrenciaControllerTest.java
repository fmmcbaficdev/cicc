package br.gov.mt.sesp.cicc.infrastructure.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OcorrenciaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("POST abre ocorrência e devolve T1")
    void abreComT1() throws Exception {
        mvc.perform(post("/ocorrencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "Roubo a mão armada agora",
                                  "gravidade": "CRITICA",
                                  "endereco": "Av. Historiador Rubens de Mendonça, Cuiabá",
                                  "latitude": -15.601411,
                                  "longitude": -56.097892,
                                  "protocolo": "CICC-2026-HTTP-001"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.protocolo").value("CICC-2026-HTTP-001"))
                .andExpect(jsonPath("$.inicioAtendimento").isNotEmpty());
    }

    @Test
    @DisplayName("POST com protocolo repetido devolve 422")
    void recusaProtocoloDuplicado() throws Exception {
        final var corpo = """
                {
                  "descricao": "Furto passado",
                  "gravidade": "BAIXA",
                  "endereco": "Centro, Cuiabá",
                  "latitude": -15.6,
                  "longitude": -56.1,
                  "protocolo": "CICC-2026-HTTP-DUP"
                }
                """;

        mvc.perform(post("/ocorrencias").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated());

        mvc.perform(post("/ocorrencias").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Já existe ocorrência com este protocolo"));
    }

    @Test
    @DisplayName("GET / explica como abrir o chamado")
    void raizExplicaAApi() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.abrir").value("POST /ocorrencias"))
                .andExpect(jsonPath("$.chamada").value("GET /pabx/chamada"));
    }

    @Test
    @DisplayName("GET /ocorrencias/{id} devolve o T1 depois do POST")
    void buscaPeloId() throws Exception {
        final var criado = mvc.perform(post("/ocorrencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "Roubo a mão armada agora",
                                  "gravidade": "CRITICA",
                                  "endereco": "Centro, Cuiabá",
                                  "latitude": -15.6,
                                  "longitude": -56.1,
                                  "protocolo": "CICC-2026-HTTP-GET"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        final var location = criado.getResponse().getHeader("Location");

        mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.protocolo").value("CICC-2026-HTTP-GET"))
                .andExpect(jsonPath("$.inicioAtendimento").isNotEmpty());
    }

    @Test
    @DisplayName("POST encaminha o cartão à mesa sem matching")
    void encaminhaAMesa() throws Exception {
        final var criado = mvc.perform(post("/ocorrencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "Roubo a mão armada agora",
                                  "gravidade": "CRITICA",
                                  "endereco": "Centro, Cuiabá",
                                  "latitude": -15.6,
                                  "longitude": -56.1,
                                  "protocolo": "CICC-2026-HTTP-MESA"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        final var location = criado.getResponse().getHeader("Location");

        mvc.perform(post(location + "/encaminhar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mesa\":\"CBA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.situacao").value("NA_MESA"))
                .andExpect(jsonPath("$.mesa").value("CBA"));

        mvc.perform(get("/mesa/CBA/ocorrencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].protocolo").value("CICC-2026-HTTP-MESA"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("GET sugere Livre e POST empenha com T2")
    void sugereEEmpenha() throws Exception {
        final var criado = mvc.perform(post("/ocorrencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "descricao": "Roubo a mão armada agora",
                                  "gravidade": "CRITICA",
                                  "endereco": "Av. Historiador Rubens de Mendonça, Cuiabá",
                                  "latitude": -15.601411,
                                  "longitude": -56.097892,
                                  "protocolo": "CICC-2026-HTTP-T2"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        final var location = criado.getResponse().getHeader("Location");

        mvc.perform(post(location + "/encaminhar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mesa\":\"CBA\"}"))
                .andExpect(status().isOk());

        mvc.perform(get(location + "/viaturas-sugeridas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].prefixo").value("PM-CBA-01"));

        mvc.perform(get(location + "/viaturas-sugeridas?ampliar=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[2].prefixo").value("PM-CBA-03"));

        mvc.perform(post(location + "/empenhar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"prefixo\":\"PM-CBA-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.situacao").value("EMPENHADA"))
                .andExpect(jsonPath("$.prefixoEmpenhado").value("PM-CBA-01"))
                .andExpect(jsonPath("$.inicioDeslocamento").isNotEmpty());

        mvc.perform(post(location + "/no-local"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.situacao").value("NO_LOCAL"))
                .andExpect(jsonPath("$.noLocalEm").isNotEmpty())
                .andExpect(jsonPath("$.noLocalManual").value(true));
    }
}
