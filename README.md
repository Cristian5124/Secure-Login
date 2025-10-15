# Secure Login System - Sistema de Autenticación Seguro
Una aplicación web completa construida con Spring Boot y Apache HTTP Server, desplegada en AWS EC2 con arquitectura de dos capas, implementando múltiples medidas de seguridad incluyendo HTTPS, BCrypt y arquitectura distribuida.

![Java](https://img.shields.io/badge/Java-11-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.0-brightgreen) ![AWS](https://img.shields.io/badge/AWS-EC2-yellow) ![License](https://img.shields.io/badge/License-MIT-blue)

## Resumen

Este proyecto implementa un sistema completo de autenticación de usuarios usando Spring Boot 2.7.0 con cifrado BCrypt y un frontend servido por Apache HTTP Server en infraestructura AWS. La aplicación está desplegada en dos instancias EC2 separadas, demostrando una arquitectura de dos capas con comunicaciones HTTPS end-to-end para garantizar la seguridad de las credenciales y datos transmitidos.

## Características

**Backend:** API REST versionada con endpoints `/api/v1/*`, cifrado de contraseñas mediante BCrypt, validación de credenciales, códigos de estado HTTP apropiados (200, 401, 500), CORS configurado para comunicación cross-origin, y servidor HTTPS en puerto 8443 con keystore PKCS12.

**Frontend:** Interfaz HTML5 con formularios semánticos, JavaScript vanilla utilizando Fetch API con async/await, estilos CSS3 responsivos, servidor Apache HTTP con certificados SSL/TLS en puerto 443, y manejo de errores con feedback visual para el usuario.

**Características:** Arquitectura de dos capas con separación de responsabilidades, comunicaciones cifradas HTTPS end-to-end, gestión segura de claves privadas mediante archivos .pem con permisos restrictivos, grupos de seguridad AWS actuando como firewall virtual, y manejo de errores sin exposición de información sensible.

## Arquitectura

Desarrollo Local
```
┌─────────────────────┐    ┌─────────────────┐
│       Frontend      │    │     Backend     │
│    (HTML/CSS/JS)    │    │     REST API    │
│      index.html     │───▶│     /api/v1/*   │
│      Port: 8080     │    │   Spring Boot   │
│      localhost      │    │  localhost:8080 │
└─────────────────────┘    └─────────────────┘
```

Producción AWS
```
┌─────────────────────────────────────────────────────────────────┐
│                          AWS CLOUD                              │
│                                                                 │
│  ┌─────────────────────┐    ┌─────────────────────────────────┐ │
│  │      EC2 Instance   │    │            EC2 Instance         │ │
│  │   Apache Frontend   │◄──►│         Spring Boot API         │ │
│  │   Port: 80, 443     │    │       Port: 8080, 8443          │ │
│  │   54.235.36.56      │    │        54.208.125.139           │ │
│  └─────────────────────┘    └─────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

Flujo de Datos
```
┌──────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│ LoginController  │───▶│   UserService    │───▶│  BCrypt Hasher  │
│ (REST Endpoints) │    │ (Business Logic) │    │  (Validation)   │
└──────────────────┘    └──────────────────┘    └─────────────────┘
        │
        ▼
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Web Browser   │───▶│     Fetch API    │───▶│   JSON Response │
│   (Cliente)     │    │   (AJAX Calls)   │    │    (API Data)   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

## Despliegue en AWS

Demostración en Vivo

Aplicación Web: https://54.235.36.56

API REST: https://54.208.125.139:8443/api/v1/status

Nota: Los certificados SSL son autofirmados. Tu navegador mostrará una advertencia de seguridad. Haz clic en "Avanzado" y luego "Continuar al sitio" para acceder.

Arquitectura AWS
```
┌─────────────────────────────────────────────────────────────────┐
│                         AWS CLOUD                               │
│                                                                 │
│  ┌─────────────────────┐    ┌─────────────────────────────────┐ │
│  │      EC2 Instance   │    │         EC2 Instance            │ │
│  │   t2.micro (1GB)    │◄──►│  t2.micro (1GB)                 │ │
│  │                     │    │                                 │ │
│  │  Apache httpd 2.4   │    │  Spring Boot 2.7.0              │ │
│  │  Port: 443 (HTTPS)  │    │  Port: 8443 (HTTPS)             │ │
│  │  SSL Certificate    │    │  PKCS12 Keystore                │ │
│  │  Amazon Linux 2023  │    │  Java 11                        │ │
│  │                     │    │  Amazon Linux 2023              │ │
│  │  Public IP:         │    │                                 │ │
│  │  54.235.36.56       │    │  Public IP:                     │ │
│  │                     │    │  54.208.125.139                 │ │
│  └─────────────────────┘    └─────────────────────────────────┘ │
│           │                                                     │
│           ▼                                                     │
│  ┌─────────────────────┐                                        │
│  │   Security Groups   │                                        │
│  │  HTTP: 80, 443      │                                        │
│  │  SSH: 22            │                                        │
│  │  Custom: 8080, 8443 │                                        │
│  └─────────────────────┘                                        │
└─────────────────────────────────────────────────────────────────┘
```

Configuración de Producción

Instancia EC2 - Apache:

Tipo de instancia: t2.micro (1 vCPU, 1 GB de RAM)
AMI: Amazon Linux 2023
IP pública: 54.235.36.56
Región: us-east-1 (Virginia)
Software: Apache HTTP Server 2.4
Puertos: 22 (SSH), 80 (HTTP), 443 (HTTPS)

Instancia EC2 - Spring Boot:

Tipo de instancia: t2.micro (1 vCPU, 1 GB de RAM)
AMI: Amazon Linux 2023
IP pública: 54.208.125.139
Región: us-east-1 (Virginia)
Software: Java 11, Maven 3.8+
Puertos: 22 (SSH), 8080 (HTTP), 8443 (HTTPS)

## API Endpoints

| Método | Endpoint | Parámetros | Descripción | Status Codes |
|--------|----------|------------|-------------|--------------|
| GET | `/api/v1/status` | Ninguno | Verificar estado del servidor | 200, 500 |
| POST | `/api/v1/login` | JSON body | Autenticar usuario | 200, 401, 500 |

### Ejemplo de Request - Login
```bash
curl -X POST https://54.208.125.139:8443/api/v1/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin"
  }'
```

### Ejemplo de Response - Login Exitoso
```json
{
  "success": true,
  "message": "Login exitoso",
  "username": "admin"
}
```

### Ejemplo de Response - Login Fallido
```json
{
  "success": false,
  "message": "Credenciales inválidas"
}
```

## Requisitos de Seguridad Implementados

Este proyecto implementa múltiples capas de seguridad siguiendo las mejores prácticas de la industria. En primer lugar, se utiliza BCrypt, un algoritmo de hashing adaptativo diseñado específicamente para almacenar contraseñas de forma segura, que hace computacionalmente inviable revertir el hash para obtener la contraseña original. Las comunicaciones entre el cliente y los servidores están protegidas mediante protocolo HTTPS con certificados SSL/TLS, garantizando que toda la información transmitida (incluidas credenciales de usuario) viaja cifrada por la red, previniendo ataques de interceptación (man-in-the-middle). La arquitectura implementa separación de responsabilidades mediante dos servidores EC2 independientes: uno para el frontend (Apache) y otro para el backend (Spring Boot), lo que permite aislar las capas de presentación y lógica de negocio, reduciendo la superficie de ataque. El control de acceso se gestiona mediante grupos de seguridad de AWS que actúan como firewall virtual, permitiendo únicamente el tráfico en los puertos necesarios (22 para SSH, 80 y 443 para HTTP/HTTPS, y 8080/8443 para el backend). Las claves privadas de acceso SSH (archivos .pem) están protegidas con permisos restrictivos y no se suben al repositorio, manteniéndose únicamente en el entorno local del administrador. Finalmente, la aplicación implementa CORS (Cross-Origin Resource Sharing) de forma controlada para permitir únicamente las comunicaciones entre el frontend y backend autorizados, mientras que las respuestas de autenticación incluyen mensajes descriptivos pero sin revelar información sensible que pueda ser explotada por atacantes.

## Implementación

Prerrequisitos

Java 11+
Maven 3.6+
Git
Cuenta AWS con acceso a EC2
Cliente SSH

Ejecución Local

Clonar el repositorio

```bash
git clone https://github.com/Cristian5124/Secure-Login.git
```

Ir a la carpeta del proyecto

```bash
cd Secure-Login/spring-backend
```

Compilar el proyecto

```bash
mvn clean package
```

Ejecutar la aplicación

```bash
java -jar target/login-backend-1.0.0.jar
```

Acceder en:

Interfaz Web: index.html
API REST: http://localhost:8080/api/v1/status

Despliegue en AWS

Pasos de Despliegue

1. Configuración de Instancias EC2

Crear instancia EC2 desde AWS Console
AMI: Amazon Linux 2023
Instance type: t2.micro
Security group: Allow ports 22 (SSH), 80/443 (HTTP/HTTPS), 8080/8443 (API)
Key pair: Descargar archivo .pem

Conectar por SSH

```bash
ssh -i "your-key.pem" ec2-user@54.235.36.56
```

2. Configuración de Servidor Apache

Instalar Apache

```bash
sudo yum update -y
sudo yum install -y httpd openssl mod_ssl
sudo systemctl start httpd
sudo systemctl enable httpd
```

Configurar HTTPS

```bash
# Generar certificado autofirmado
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout /etc/pki/tls/private/apache-selfsigned.key \
  -out /etc/pki/tls/certs/apache-selfsigned.crt

# Subir frontend
scp -i "your-key.pem" index.html ec2-user@54.235.36.56:/home/ec2-user/
sudo cp /home/ec2-user/index.html /var/www/html/

# Reiniciar Apache
sudo systemctl restart httpd
```

3. Configuración de Servidor Spring Boot

Instalar Java y Maven

```bash
sudo yum update -y
sudo yum install -y java-11-amazon-corretto maven git
```

Desplegar aplicación

```bash
# Clonar repositorio
git clone https://github.com/Cristian5124/Secure-Login.git
cd Secure-Login/spring-backend
mvn clean package

# Configurar HTTPS
keytool -genkeypair -alias springboot -keyalg RSA -keysize 2048 \
  -storetype PKCS12 -keystore spring-keystore.p12 -validity 365 \
  -storepass changeit
```

Configuración en application.properties

```properties
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=/home/ec2-user/spring-keystore.p12
server.ssl.key-store-type=PKCS12
server.ssl.key-store-password=changeit
server.ssl.key-alias=springboot
```

Ejecutar aplicación

```bash
nohup java -jar target/login-backend-1.0.0.jar \
  --spring.config.location=file:/home/ec2-user/application.properties \
  > spring-app.log 2>&1 &
```

Verificar funcionamiento

```bash
curl -k https://localhost:8443/api/v1/status
```

## Estructura del Proyecto

```
Secure-Login/
├── index.html
├── spring-backend/
│   ├── src/main/java/com/taller/login/
│   │   ├── LoginApplication.java
│   │   ├── controller/
│   │   │   └── LoginController.java
│   │   └── service/
│   │       └── UserService.java
│   ├── src/main/resources/
│   ├── target/
│   │   └── login-backend-1.0.0.jar
│   └── pom.xml
├── .gitignore
├── LICENSE
└── README.md
```

## Modelo de Datos

Usuarios Hardcodeados

```java
// LoginController.java
private static final Map<String, String> USERS = Map.of(
    "admin", "$2a$10$hash...",  // password: admin
    "root", "$2a$10$hash...",   // password: root
    "arep", "$2a$10$hash..."    // password: arep
);
```

Validaciones

username: Requerido, no puede estar vacío
password: Requerido, debe coincidir con el hash BCrypt almacenado

## Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 11 | Lenguaje de programación backend |
| Spring Boot | 2.7.0 | Framework para API REST |
| Maven | 3.8+ | Gestión de dependencias y build |
| BCrypt | (spring-security-crypto) | Cifrado de contraseñas |
| Apache HTTP Server | 2.4 | Servidor web frontend |
| HTML5/CSS3/JavaScript | ES6+ | Interfaz de usuario |
| OpenSSL | 1.1+ | Generación de certificados SSL |
| AWS EC2 | t2.micro | Infraestructura cloud |
| Amazon Linux | 2023 | Sistema operativo |

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo LICENSE para más detalles.

## Autor

Cristian David Polo Garrido - Desarrollador Full Stack

GitHub: https://github.com/Cristian5124

Proyecto: https://github.com/Cristian5124/Secure-Login

## Proyecto Académico

Este proyecto fue desarrollado como parte del curso de Arquitecturas Empresariales (AREP), demostrando la implementación de arquitecturas distribuidas seguras en la nube de AWS con Spring Boot y Apache HTTP Server.

## Demostraciones
<img width="975" height="548" alt="image" src="https://github.com/user-attachments/assets/c238ab4d-2824-4276-a39e-143f68eae15c" />
<img width="975" height="605" alt="image" src="https://github.com/user-attachments/assets/1fd53248-e7d6-446b-b75a-05e019f96fe4" />
<img width="975" height="606" alt="image" src="https://github.com/user-attachments/assets/fda58ced-257f-4ddf-961c-cdb4e09ff4d2" />

https://github.com/user-attachments/assets/8e40dcca-56b1-4826-a4f3-4ce98d3893f8



