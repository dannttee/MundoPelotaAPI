Mundo Pelota API
API REST para la plataforma de e-commerce de equipos deportivos. Backend desarrollado con Spring Boot.

🌐 Descripción
Mundo Pelota API es el servidor backend que gestiona:

Autenticación y autorización de usuarios

Gestión del catálogo de productos

Administración de carritos de compra

Procesamiento de órdenes

Perfil de usuarios

Datos de mercado (integración con API de dólar)

🏗️ Arquitectura
Stack Tecnológico
Framework: Spring Boot 3.x

Lenguaje: Java 11+

Base de Datos: MySQL/PostgreSQL

ORM: JPA/Hibernate

Seguridad: Spring Security + JWT

Validación: Jakarta Bean Validation

API Documentation: Swagger/Springdoc OpenAPI

Build Tool: Gradle

Patrón de Arquitectura
Patrón: MVC/Layered Architecture

Layers: Controller → Service → Repository → Database

DTOs: Separación entre modelos internos y API responses

Exception Handling: Manejo centralizado de errores

📋 Estructura del Proyecto
text
mundopelota-api/
├── src/main/java/com/example/mundopelota/
│   ├── controller/          # Controladores REST
│   ├── service/             # Lógica de negocio
│   ├── repository/          # Acceso a datos
│   ├── model/               # Entidades JPA
│   ├── dto/                 # Data Transfer Objects
│   ├── security/            # Configuración de seguridad
│   ├── exception/           # Excepciones personalizadas
│   ├── config/              # Configuraciones de la app
│   └── MundoPelotaApiApplication.java
├── src/main/resources/
│   ├── application.properties
│   ├── application-dev.properties
│   └── application-prod.properties
├── build.gradle
└── README.md
🔐 Autenticación
Sistema JWT (JSON Web Token)
Login retorna un token JWT válido por 24 horas

Token debe incluirse en header Authorization: Bearer <token>

Refresh token disponible para renovar sesión

Roles y Permisos
ROLE_USER: Usuario estándar (comprador)

ROLE_ADMIN: Administrador (gestión de productos)

📡 Endpoints Principales
Autenticación
Login
text
POST /api/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123"
}

Response 200:
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "email": "usuario@example.com",
    "nombre": "Juan",
    "rol": "ROLE_USER"
  }
}
Registro
text
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "password123",
  "confirmPassword": "password123"
}

Response 201: Created
Refresh Token
text
POST /api/auth/refresh
Authorization: Bearer <refresh_token>

Response 200:
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "expiresIn": 86400
}
Productos
Listar Productos
text
GET /api/productos?page=0&size=20&categoria=futbol

Response 200:
{
  "content": [
    {
      "id": 1,
      "nombre": "Balón de Fútbol",
      "descripcion": "Balón profesional",
      "precio": 45.99,
      "stock": 100,
      "categoria": "futbol",
      "imagen": "url_imagen"
    }
  ],
  "totalElements": 150,
  "totalPages": 8
}
Obtener Producto por ID
text
GET /api/productos/{id}

Response 200:
{
  "id": 1,
  "nombre": "Balón de Fútbol",
  "descripcion": "Balón profesional FIFA",
  "precio": 45.99,
  "stock": 100,
  "categoria": "futbol",
  "imagen": "url_imagen",
  "especificaciones": {
    "peso": "410-450g",
    "diametro": "68-70cm"
  }
}
Crear Producto (Admin)
text
POST /api/productos
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "Nuevo Balón",
  "descripcion": "Descripción del producto",
  "precio": 49.99,
  "stock": 50,
  "categoria": "futbol",
  "imagen": "url_imagen"
}

Response 201: Created
Actualizar Producto (Admin)
text
PUT /api/productos/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "Balón Actualizado",
  "precio": 55.99,
  "stock": 75
}

Response 200: OK
Eliminar Producto (Admin)
text
DELETE /api/productos/{id}
Authorization: Bearer <token>

Response 204: No Content
Carrito
Obtener Carrito del Usuario
text
GET /api/carrito
Authorization: Bearer <token>

Response 200:
{
  "id": 1,
  "usuario_id": 1,
  "items": [
    {
      "id": 101,
      "producto_id": 1,
      "cantidad": 2,
      "precioUnitario": 45.99,
      "subtotal": 91.98
    }
  ],
  "total": 91.98
}
Agregar Item al Carrito
text
POST /api/carrito/items
Authorization: Bearer <token>
Content-Type: application/json

{
  "producto_id": 1,
  "cantidad": 2
}

Response 201: Created
Actualizar Cantidad en Carrito
text
PUT /api/carrito/items/{itemId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "cantidad": 5
}

Response 200: OK
Eliminar Item del Carrito
text
DELETE /api/carrito/items/{itemId}
Authorization: Bearer <token>

Response 204: No Content
Vaciar Carrito
text
DELETE /api/carrito
Authorization: Bearer <token>

Response 204: No Content
Órdenes
Procesar Compra (Checkout)
text
POST /api/ordenes/checkout
Authorization: Bearer <token>
Content-Type: application/json

{
  "direccionEnvio": "Calle Principal 123",
  "ciudad": "Santiago",
  "codigoPostal": "8320000",
  "metadoPago": "tarjeta_credito"
}

Response 201: Created
{
  "id": 1,
  "numero_orden": "ORD-2025-001",
  "fecha": "2025-12-14T19:47:00Z",
  "estado": "PENDIENTE",
  "total": 91.98,
  "items": [...]
}
Obtener Historial de Órdenes
text
GET /api/ordenes?page=0&size=10
Authorization: Bearer <token>

Response 200:
{
  "content": [
    {
      "id": 1,
      "numero_orden": "ORD-2025-001",
      "fecha": "2025-12-14",
      "estado": "COMPLETADA",
      "total": 91.98
    }
  ],
  "totalElements": 5
}
Obtener Detalles de Orden
text
GET /api/ordenes/{id}
Authorization: Bearer <token>

Response 200:
{
  "id": 1,
  "numero_orden": "ORD-2025-001",
  "fecha": "2025-12-14T19:47:00Z",
  "estado": "COMPLETADA",
  "total": 91.98,
  "items": [...],
  "cliente": {...},
  "envio": {...}
}
Usuario
Obtener Perfil
text
GET /api/usuarios/perfil
Authorization: Bearer <token>

Response 200:
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "telefono": "+56 9 12345678",
  "direccion": "Calle Principal 123",
  "ciudad": "Santiago",
  "rol": "ROLE_USER"
}
Actualizar Perfil
text
PUT /api/usuarios/perfil
Authorization: Bearer <token>
Content-Type: application/json

{
  "nombre": "Juan Carlos Pérez",
  "telefono": "+56 9 87654321",
  "direccion": "Calle Nueva 456"
}

Response 200: OK
Cambiar Contraseña
text
POST /api/usuarios/cambiar-contraseña
Authorization: Bearer <token>
Content-Type: application/json

{
  "contraseña_actual": "oldpassword123",
  "contraseña_nueva": "newpassword123",
  "confirmar_contraseña": "newpassword123"
}

Response 200: OK
🚀 Instalación y Configuración
Requisitos Previos
Java 11+

MySQL 8.0+ o PostgreSQL 12+

Gradle 7.0+

Git

Pasos de Instalación
1. Clonar Repositorio
bash
git clone <repository-url>
cd mundopelota-api
2. Configurar Base de Datos
Crea la base de datos en MySQL:

sql
CREATE DATABASE mundopelota_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'mundopelota'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON mundopelota_db.* TO 'mundopelota'@'localhost';
FLUSH PRIVILEGES;
3. Configurar Propiedades
Edita application.properties:

text
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/mundopelota_db
spring.datasource.username=mundopelota
spring.datasource.password=password123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT
jwt.secret=tu_clave_secreta_muy_larga_y_segura_aqui_123456789
jwt.expiration=86400000

# Server
server.port=8080
server.servlet.context-path=/api
4. Ejecutar la Aplicación
bash
./gradlew bootRun
O ejecutar desde IDE:

Click derecho en MundoPelotaApiApplication.java

Seleccionar "Run"

Acceder a la API
Base URL: http://localhost:8080/api

Swagger UI: http://localhost:8080/api/swagger-ui.html

OpenAPI Docs: http://localhost:8080/api/v3/api-docs

🧪 Testing
Ejecutar Tests Unitarios
bash
./gradlew test
Ejecutar Tests de Integración
bash
./gradlew test --tests *IntegrationTest
Coverage de Código
bash
./gradlew test jacocoTestReport
📦 Dependencias Principales
text
dependencies {
    // Spring Boot
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Database
    runtimeOnly 'com.mysql:mysql-connector-j'
    implementation 'org.flywaydb:flyway-core'
    implementation 'org.flywaydb:flyway-mysql'

    // JWT
    implementation 'io.jsonwebtoken:jjwt:0.11.5'

    // Documentation
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2'

    // Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
}
🐛 Troubleshooting
Error: Connection refused
Solución: Verifica que MySQL esté corriendo:

bash
# Linux/Mac
sudo systemctl start mysql

# O verifica la conexión
mysql -u mundopelota -p
Error: Table doesn't exist
Solución: Las tablas se crean automáticamente. Si no aparecen:

Verifica spring.jpa.hibernate.ddl-auto=update en properties

Reinicia la aplicación

Revisa los logs

Error: Invalid JWT Token
Solución:

Token expirado: Usa refresh token

Token inválido: Verifica que incluya "Bearer " en el header

Clave secreta diferente: Asegúrate de usar la misma en cliente y servidor

📊 Estadísticas de la API
Recurso	Métodos	Auth	Descripción
/auth	POST	No	Autenticación
/productos	GET,POST,PUT,DELETE	Parcial	Gestión de productos
/carrito	GET,POST,DELETE	Sí	Carrito de compras
/ordenes	GET,POST	Sí	Gestión de órdenes
/usuarios	GET,PUT,POST	Sí	Perfil de usuario
🔄 CI/CD
GitHub Actions (Ejemplo)
text
name: Build and Test
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up Java
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Build with Gradle
        run: ./gradlew build
📚 Documentación Adicional
Spring Boot Documentation

JPA Documentation

Spring Security

JWT.io

👥 Contribuciones
Fork el proyecto

Crea una rama para tu feature (git checkout -b feature/nueva-caracteristica)

Commit tus cambios (git commit -m 'Agregar nueva característica')

Push a la rama (git push origin feature/nueva-caracteristica)

Abre un Pull Request

📄 Licencia
Este proyecto está bajo licencia MIT. Ver archivo LICENSE para más detalles.

👤 Autores
Desarrollador Principal: Dante Muñoz

Institución: Duoc UC

Fecha: Diciembre 2025

Última actualización: Diciembre 14, 2025
