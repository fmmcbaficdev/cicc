package br.gov.mt.sesp.cicc.infrastructure.config;

import br.gov.mt.sesp.cicc.application.cad.AbrirOcorrenciaUseCase;
import br.gov.mt.sesp.cicc.application.cad.BuscarOcorrenciaUseCase;
import br.gov.mt.sesp.cicc.application.cad.EncaminharOcorrenciaUseCase;
import br.gov.mt.sesp.cicc.application.pabx.ConsultarChamadaPabxUseCase;
import br.gov.mt.sesp.cicc.application.sala.EmpenharViaturaUseCase;
import br.gov.mt.sesp.cicc.application.sala.EncerrarOcorrenciaUseCase;
import br.gov.mt.sesp.cicc.application.sala.ListarOcorrenciasNaMesaUseCase;
import br.gov.mt.sesp.cicc.application.sala.RegistrarNoLocalUseCase;
import br.gov.mt.sesp.cicc.application.sala.SugerirViaturasUseCase;
import br.gov.mt.sesp.cicc.domain.avl.AvlPort;
import br.gov.mt.sesp.cicc.domain.cad.OcorrenciaRepository;
import br.gov.mt.sesp.cicc.domain.pabx.PabxPort;
import br.gov.mt.sesp.cicc.infrastructure.avl.MockAvlAdapter;
import br.gov.mt.sesp.cicc.infrastructure.pabx.MockPabxAdapter;
import br.gov.mt.sesp.cicc.infrastructure.persistence.InMemoryOcorrenciaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UseCaseConfig {

    @Bean
    OcorrenciaRepository ocorrenciaRepository() {
        return new InMemoryOcorrenciaRepository();
    }

    @Bean
    PabxPort pabxPort(@Value("${cicc.pabx.mock-mudo:false}") final boolean mudo) {
        return new MockPabxAdapter(mudo, Clock.systemUTC());
    }

    @Bean
    AvlPort avlPort() {
        return new MockAvlAdapter();
    }

    @Bean
    AbrirOcorrenciaUseCase abrirOcorrenciaUseCase(
            final OcorrenciaRepository ocorrenciaRepository,
            final PabxPort pabxPort
    ) {
        return new AbrirOcorrenciaUseCase(ocorrenciaRepository, pabxPort);
    }

    @Bean
    BuscarOcorrenciaUseCase buscarOcorrenciaUseCase(final OcorrenciaRepository ocorrenciaRepository) {
        return new BuscarOcorrenciaUseCase(ocorrenciaRepository);
    }

    @Bean
    ConsultarChamadaPabxUseCase consultarChamadaPabxUseCase(final PabxPort pabxPort) {
        return new ConsultarChamadaPabxUseCase(pabxPort);
    }

    @Bean
    EncaminharOcorrenciaUseCase encaminharOcorrenciaUseCase(final OcorrenciaRepository ocorrenciaRepository) {
        return new EncaminharOcorrenciaUseCase(ocorrenciaRepository);
    }

    @Bean
    ListarOcorrenciasNaMesaUseCase listarOcorrenciasNaMesaUseCase(final OcorrenciaRepository ocorrenciaRepository) {
        return new ListarOcorrenciasNaMesaUseCase(ocorrenciaRepository);
    }

    @Bean
    SugerirViaturasUseCase sugerirViaturasUseCase(
            final OcorrenciaRepository ocorrenciaRepository,
            final AvlPort avlPort
    ) {
        return new SugerirViaturasUseCase(ocorrenciaRepository, avlPort);
    }

    @Bean
    EmpenharViaturaUseCase empenharViaturaUseCase(
            final OcorrenciaRepository ocorrenciaRepository,
            final AvlPort avlPort
    ) {
        return new EmpenharViaturaUseCase(ocorrenciaRepository, avlPort);
    }

    @Bean
    RegistrarNoLocalUseCase registrarNoLocalUseCase(final OcorrenciaRepository ocorrenciaRepository) {
        return new RegistrarNoLocalUseCase(ocorrenciaRepository);
    }

    @Bean
    EncerrarOcorrenciaUseCase encerrarOcorrenciaUseCase(
            final OcorrenciaRepository ocorrenciaRepository,
            final AvlPort avlPort
    ) {
        return new EncerrarOcorrenciaUseCase(ocorrenciaRepository, avlPort);
    }
}
