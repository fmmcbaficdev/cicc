package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PontoTest {

    @Test
    @DisplayName("aceita lat/long válidos sem tipo Spatial")
    void aceitaCoordenadasValidas() {
        final var ponto = new Ponto(-15.601411, -56.097892);

        assertEquals(-15.601411, ponto.latitude());
        assertEquals(-56.097892, ponto.longitude());
    }

    @Test
    @DisplayName("distância haversine do mesmo ponto é zero")
    void distanciaDoMesmoPontoEZero() {
        final var ponto = new Ponto(-15.601411, -56.097892);

        assertEquals(0, ponto.distanciaEmMetros(ponto), 0.01);
    }

    @Test
    @DisplayName("recusa latitude ou longitude fora do intervalo")
    void recusaCoordenadasInvalidas() {
        assertEquals("Latitude inválida",
                assertThrows(ValidationException.class, () -> new Ponto(91, 0)).getMessage());
        assertEquals("Longitude inválida",
                assertThrows(ValidationException.class, () -> new Ponto(0, -181)).getMessage());
        assertEquals("Latitude inválida",
                assertThrows(ValidationException.class, () -> new Ponto(Double.NaN, 0)).getMessage());
    }
}
