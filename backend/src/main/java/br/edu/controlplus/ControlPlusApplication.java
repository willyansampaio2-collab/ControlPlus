package br.edu.controlplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Ponto de entrada: o Spring Boot inicia o servidor da API.
@SpringBootApplication
public class ControlPlusApplication {
    public static void main(String[] args) {
        SpringApplication.run(ControlPlusApplication.class, args);
    }
}
