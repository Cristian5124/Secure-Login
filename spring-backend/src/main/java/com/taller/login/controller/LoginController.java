package com.taller.login.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que gestiona las operaciones de autenticación de usuarios.
 * Proporciona endpoints para verificar el estado del servidor y realizar login.
 * Utiliza BCrypt para el cifrado seguro de contraseñas.
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class LoginController {

    private final Map<String, String> users = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Constructor que inicializa los usuarios por defecto del sistema.
     * Crea tres usuarios hardcodeados (admin, root, arep) con sus contraseñas
     * cifradas mediante BCrypt.
     */
    public LoginController() {
        users.put("admin", passwordEncoder.encode("admin"));

        users.put("root", passwordEncoder.encode("root"));

        users.put("arep", passwordEncoder.encode("arep"));

        System.out.println("Usuarios registrados:");
        users.forEach(
                (user, hash) -> System.out.println("Usuario: " + user + " | Hash: " + hash.substring(0, 20) + "..."));
    }

    /**
     * Endpoint que verifica el estado del servidor.
     * 
     * @return mensaje indicando que el servidor está funcionando
     */
    @GetMapping("/status")
    public String getStatus() {
        return "Servidor Spring funcionando";
    }

    /**
     * Endpoint para autenticar usuarios en el sistema.
     * Valida las credenciales comparando la contraseña ingresada con el hash BCrypt
     * almacenado.
     * 
     * @param loginRequest objeto que contiene el username y password del usuario
     * @return ResponseEntity con el resultado de la autenticación (200 OK si es
     *         exitoso, 401 si falla, 400 si faltan datos)
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        Map<String, String> response = new HashMap<>();

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || password == null) {
            response.put("message", "Usuario y contraseña son requeridos");
            return ResponseEntity.badRequest().body(response);
        }

        if (users.containsKey(username)) {
            String storedHash = users.get(username);

            if (passwordEncoder.matches(password, storedHash)) {
                response.put("message", "Bienvenido " + username);
                response.put("status", "success");
                System.out.println("Login exitoso para usuario: " + username);
                return ResponseEntity.ok(response);
            }
        }

        response.put("message", "Usuario o contraseña incorrectos");
        response.put("status", "error");
        System.out.println("Login fallido para usuario: " + username);
        return ResponseEntity.status(401).body(response);
    }

    /**
     * Clase interna que representa la estructura de la petición de login.
     * Contiene los campos username y password necesarios para la autenticación.
     */
    public static class LoginRequest {
        private String username;
        private String password;

        /**
         * Obtiene el nombre de usuario.
         * 
         * @return el nombre de usuario
         */
        public String getUsername() {
            return username;
        }

        /**
         * Establece el nombre de usuario.
         * 
         * @param username el nombre de usuario a establecer
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * Obtiene la contraseña.
         * 
         * @return la contraseña
         */
        public String getPassword() {
            return password;
        }

        /**
         * Establece la contraseña.
         * 
         * @param password la contraseña a establecer
         */
        public void setPassword(String password) {
            this.password = password;
        }
    }
}