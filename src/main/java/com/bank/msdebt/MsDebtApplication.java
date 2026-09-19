package com.bank.msdebt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Microservicio de gestion de deuda vencida.
 */
@EnableMongoAuditing
@SpringBootApplication
public class MsDebtApplication {

    /**
     * Inicio de la aplicacion.
     *
     * @param args argumentos de inicio
     */
    public static void main(String[] args) {
        SpringApplication.run(MsDebtApplication.class, args);
    }
}
