package br.gov.mt.sesp.cicc.infrastructure.rest;

import br.gov.mt.sesp.cicc.infrastructure.geo.MockGeocodificacaoAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GeocodificacaoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("GET /geocodificacao/endereco escreve o endereço do ponto do celular")
    void enderecoDoPontoDoCelular() throws Exception {
        mvc.perform(get("/geocodificacao/endereco")
                        .param("latitude", String.valueOf(MockGeocodificacaoAdapter.PONTO_CELULAR.latitude()))
                        .param("longitude", String.valueOf(MockGeocodificacaoAdapter.PONTO_CELULAR.longitude())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endereco").value(MockGeocodificacaoAdapter.ENDERECO_CELULAR));
    }

    @Test
    @DisplayName("GET /geocodificacao/ponto acha marco conhecido")
    void pontoDeReferencia() throws Exception {
        mvc.perform(get("/geocodificacao/ponto").param("texto", "Shopping Estação"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude").value(MockGeocodificacaoAdapter.PONTO_REFERENCIA.latitude()))
                .andExpect(jsonPath("$.longitude").value(MockGeocodificacaoAdapter.PONTO_REFERENCIA.longitude()));
    }

    @Test
    @DisplayName("GET /geocodificacao/ponto desconhecido devolve 404")
    void pontoDesconhecido() throws Exception {
        mvc.perform(get("/geocodificacao/ponto").param("texto", "rua sem nome"))
                .andExpect(status().isNotFound());
    }
}
