package br.gov.mt.sesp.cicc.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UseCaseSkeletonTest {

    @Test
    @DisplayName("UseCase.execute devolve a saída do caso")
    void executeDevolveSaida() {
        var caso = new UseCase<String, String>() {
            @Override
            public String execute(final String input) {
                return input;
            }
        };
        assertEquals("ok", caso.execute("ok"));
    }
}
