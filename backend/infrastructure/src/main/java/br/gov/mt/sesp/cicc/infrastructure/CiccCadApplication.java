package br.gov.mt.sesp.cicc.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CiccCadApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CiccCadApplication.class, args);
    }
}
