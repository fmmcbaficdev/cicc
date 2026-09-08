package br.gov.mt.sesp.cicc.domain.cad;

import br.gov.mt.sesp.cicc.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MesaRegiaoTest {

    @Test
    @DisplayName("aceita as mesas do piloto CBA, VG e RDO")
    void aceitaMesasDoPiloto() {
        assertEquals("CBA", new MesaRegiao("cba").value());
        assertEquals("VG", new MesaRegiao("VG").value());
        assertEquals("RDO", new MesaRegiao(" rdo ").value());
    }

    @Test
    @DisplayName("recusa mesa fora do piloto")
    void recusaMesaDesconhecida() {
        assertEquals("Mesa da região inválida",
                assertThrows(ValidationException.class, () -> new MesaRegiao("CUIABA")).getMessage());
    }
}
