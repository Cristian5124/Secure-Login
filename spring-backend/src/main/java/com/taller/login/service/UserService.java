package com.taller.login.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona las operaciones relacionadas con usuarios.
 * Proporciona funcionalidades para registrar usuarios, validar credenciales
 * y gestionar el almacenamiento de contraseñas cifradas con BCrypt.
 */
@Service
public class UserService {

    private final Map<String, String> users = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Constructor que inicializa el servicio de usuarios.
     * Crea automáticamente los usuarios por defecto del sistema.
     */
    public UserService() {
        initializeDefaultUsers();
    }

    /**
     * Inicializa los usuarios por defecto del sistema.
     * Registra tres usuarios hardcodeados (admin, root, arep) para propósitos de
     * demostración.
     */
    private void initializeDefaultUsers() {
        registerUser("admin", "admin");
        registerUser("root", "root");
        registerUser("arep", "arep");

        System.out.println("\n=== Usuarios registrados en el sistema ===");
        users.forEach(
                (user, hash) -> System.out.println("Usuario: " + user + " | Hash: " + hash.substring(0, 20) + "..."));
        System.out.println("==========================================\n");
    }

    /**
     * Registra un nuevo usuario con su contraseña hasheada
     * 
     * @param username      nombre de usuario
     * @param plainPassword contraseña en texto plano (será hasheada)
     * @return true si se registró exitosamente, false si el usuario ya existe
     */
    public boolean registerUser(String username, String plainPassword) {
        if (users.containsKey(username)) {
            return false;
        }
        String hashedPassword = passwordEncoder.encode(plainPassword);
        users.put(username, hashedPassword);
        return true;
    }

    /**
     * Valida las credenciales del usuario
     * 
     * @param username      nombre de usuario
     * @param plainPassword contraseña en texto plano
     * @return true si las credenciales son válidas
     */
    public boolean validateCredentials(String username, String plainPassword) {
        if (username == null || plainPassword == null) {
            return false;
        }

        if (!users.containsKey(username)) {
            return false;
        }

        String storedHash = users.get(username);
        return passwordEncoder.matches(plainPassword, storedHash);
    }

    /**
     * Verifica si un usuario existe
     * 
     * @param username nombre de usuario
     * @return true si el usuario existe
     */
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    /**
     * Obtiene el número total de usuarios registrados
     * 
     * @return cantidad de usuarios
     */
    public int getUserCount() {
        return users.size();
    }
}
