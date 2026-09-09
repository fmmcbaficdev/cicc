package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.infrastructure.pabx.MockPabxAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ChamadaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("GET /pabx/chamada devolve uid e telefone do mock")
    void devolveChamadaDoMock() throws Exception {
        mvc.perform(get("/pabx/chamada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value(MockPabxAdapter.UID_AMOSTRA))
                .andExpect(jsonPath("$.telefone").value(MockPabxAdapter.TELEFONE_AMOSTRA))
                .andExpect(jsonPath("$.tronco").value(MockPabxAdapter.TRONCO_AMOSTRA))
                .andExpect(jsonPath("$.unidade").value(MockPabxAdapter.UNIDADE_AMOSTRA));
    }

    @Test
    @DisplayName("POST com pabxUid copia o telefone do mock e carimba o T1")
    void abreComTelefoneDoPabx() throws Exception {
        mvc.perform(post("/ocorrencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "natureza": "Roubo",
                                  "descricao": "Roubo a mão armada agora",
                                  "gravidade": "CRITICA",
                                  "endereco": "Centro, Cuiabá",
                                  "latitude": -15.6,
                                  "longitude": -56.1,
                                  "protocolo": "CICC-2026-HTTP-PABX",
                                  "pabxUid": "pabx-mock-190-cba-001"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.telefone").value(MockPabxAdapter.TELEFONE_AMOSTRA))
                .andExpect(jsonPath("$.pabxUid").value(MockPabxAdapter.UID_AMOSTRA))
                .andExpect(jsonPath("$.inicioAtendimento").isNotEmpty());
    }
}
