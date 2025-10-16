package com.taller.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación de login seguro.
 * Configura y arranca la aplicación Spring Boot con todas las características
 * necesarias para el sistema de autenticación con BCrypt.
 */
@SpringBootApplication
public class LoginApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     * 
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }
}