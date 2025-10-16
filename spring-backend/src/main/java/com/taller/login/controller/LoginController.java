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

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class LoginController {

    private final Map<String, String> users = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginController() {
        users.put("admin", passwordEncoder.encode("admin"));

        users.put("root", passwordEncoder.encode("root"));

        users.put("arep", passwordEncoder.encode("arep"));

        System.out.println("Usuarios registrados:");
        users.forEach(
                (user, hash) -> System.out.println("Usuario: " + user + " | Hash: " + hash.substring(0, 20) + "..."));
    }

    @GetMapping("/status")
    public String getStatus() {
        return "Servidor Spring funcionando";
    }

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

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}