Mundo Pelota API
API REST para la plataforma de e-commerce de equipos deportivos. Backend desarrollado con Spring Boot con arquitectura de microservicios.

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

Despliegue: Render (Microservicios)

Patrón de Arquitectura
Patrón: MVC/Layered Architecture + Microservicios

Layers: Controller → Service → Repository → Database

DTOs: Separación entre modelos internos y API responses

Exception Handling: Manejo centralizado de errores

🚀 Acceso a las APIs en Render
📍 MS Usuarios (Autenticación)
URL Base: https://ms-usuario-5m0i.onrender.com

Swagger UI: https://ms-usuario-5m0i.onrender.com/swagger-ui.html

OpenAPI Docs: https://ms-usuario-5m0i.onrender.com/v3/api-docs

Endpoints Principales:

POST /auth/login - Login

POST /auth/register - Registro

POST /auth/refresh - Refresh Token

GET /usuarios/perfil - Obtener perfil

PUT /usuarios/perfil - Actualizar perfil

POST /usuarios/cambiar-contraseña - Cambiar contraseña

📍 MS Catálogo (Productos)
URL Base: https://ms-catalogo-hora.onrender.com

Swagger UI: https://ms-catalogo-hora.onrender.com/swagger-ui.html

OpenAPI Docs: https://ms-catalogo-hora.onrender.com/v3/api-docs

Endpoints Principales:

GET /productos - Listar productos

GET /productos/{id} - Obtener producto

POST /productos - Crear producto (Admin)

PUT /productos/{id} - Actualizar producto (Admin)

DELETE /productos/{id} - Eliminar producto (Admin)

📍 MS Carrito (Compras)
URL Base: https://ms-carrito-zqlc.onrender.com

Swagger UI: https://ms-carrito-zqlc.onrender.com/swagger-ui.html

OpenAPI Docs: https://ms-carrito-zqlc.onrender.com/v3/api-docs

Endpoints Principales:

GET /carrito - Obtener carrito

POST /carrito/items - Agregar item

PUT /carrito/items/{itemId} - Actualizar cantidad

DELETE /carrito/items/{itemId} - Eliminar item

POST /ordenes/checkout - Procesar compra

GET /ordenes - Historial de órdenes

📋 Estructura del Proyecto
text
mundopelota-api/
├── ms-usuarios/                    # Microservicio de Usuarios
│   ├── src/main/java/com/example/mundopelota/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── security/
│   │   └── MsUsuariosApplication.java
│   └── build.gradle
│
├── ms-catalogo/                    # Microservicio de Catálogo
│   ├── src/main/java/com/example/mundopelota/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   └── MsCatalogoApplication.java
│   └── build.gradle
│
├── ms-carrito/                     # Microservicio de Carrito
│   ├── src/main/java/com/example/mundopelota/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   └── MsCarritoApplication.java
│   └── build.gradle
│
└── docker-compose.yml              # Orquestación local
🔐 Autenticación
Sistema JWT (JSON Web Token)
Login retorna un token JWT válido por 24 horas

Token debe incluirse en header Authorization: Bearer <token>

Refresh token disponible para renovar sesión

Roles y Permisos
ROLE_USER: Usuario estándar (comprador)

ROLE_ADMIN: Administrador (gestión de productos)

📡 Endpoints Detallados
🔑 MS USUARIOS - Autenticación
Login
text
POST https://ms-usuario-5m0i.onrender.com/auth/login
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
POST https://ms-usuario-5m0i.onrender.com/auth/register
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
POST https://ms-usuario-5m0i.onrender.com/auth/refresh
Authorization: Bearer <refresh_token>

Response 200:
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "expiresIn": 86400
}
Obtener Perfil
text
GET https://ms-usuario-5m0i.onrender.com/usuarios/perfil
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
PUT https://ms-usuario-5m0i.onrender.com/usuarios/perfil
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
POST https://ms-usuario-5m0i.onrender.com/usuarios/cambiar-contraseña
Authorization: Bearer <token>
Content-Type: application/json

{
  "contraseña_actual": "oldpassword123",
  "contraseña_nueva": "newpassword123",
  "confirmar_contraseña": "newpassword123"
}

Response 200: OK
📦 MS CATÁLOGO - Productos
Listar Productos
text
GET https://ms-catalogo-hora.onrender.com/productos?page=0&size=20&categoria=futbol

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
GET https://ms-catalogo-hora.onrender.com/productos/{id}

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
POST https://ms-catalogo-hora.onrender.com/productos
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
PUT https://ms-catalogo-hora.onrender.com/productos/{id}
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
DELETE https://ms-catalogo-hora.onrender.com/productos/{id}
Authorization: Bearer <token>

Response 204: No Content
🛒 MS CARRITO - Compras
Obtener Carrito del Usuario
text
GET https://ms-carrito-zqlc.onrender.com/carrito
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
POST https://ms-carrito-zqlc.onrender.com/carrito/items
Authorization: Bearer <token>
Content-Type: application/json

{
  "producto_id": 1,
  "cantidad": 2
}

Response 201: Created
Actualizar Cantidad en Carrito
text
PUT https://ms-carrito-zqlc.onrender.com/carrito/items/{itemId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "cantidad": 5
}

Response 200: OK
Eliminar Item del Carrito
text
DELETE https://ms-carrito-zqlc.onrender.com/carrito/items/{itemId}
Authorization: Bearer <token>

Response 204: No Content
Vaciar Carrito
text
DELETE https://ms-carrito-zqlc.onrender.com/carrito
Authorization: Bearer <token>

Response 204: No Content
📋 MS CARRITO - Órdenes
Procesar Compra (Checkout)
text
POST https://ms-carrito-zqlc.onrender.com/ordenes/checkout
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
GET https://ms-carrito-zqlc.onrender.com/ordenes?page=0&size=10
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
GET https://ms-carrito-zqlc.onrender.com/ordenes/{id}
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
🏠 Acceso Local (Desarrollo)
Si ejecutas los microservicios localmente con Docker:

bash
docker-compose up -d
Las APIs estarían disponibles en:

MS Usuarios: http://localhost:8081

MS Catálogo: http://localhost:8082

MS Carrito: http://localhost:8083

📊 Tabla Resumen de Microservicios
Microservicio	URL Base	Swagger	Función Principal
MS Usuarios	https://ms-usuario-5m0i.onrender.com	Link	Auth + Perfil
MS Catálogo	https://ms-catalogo-hora.onrender.com	Link	Productos
MS Carrito	https://ms-carrito-zqlc.onrender.com	Link	Carrito + Órdenes
🧪 Testing
Probar Endpoints con cURL
Login
bash
curl -X POST https://ms-usuario-5m0i.onrender.com/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@example.com",
    "password": "password123"
  }'
Listar Productos
bash
curl -X GET "https://ms-catalogo-hora.onrender.com/productos?page=0&size=10"
Agregar al Carrito (requiere token)
bash
curl -X POST https://ms-carrito-zqlc.onrender.com/carrito/items \
  -H "Authorization: Bearer <tu_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "producto_id": 1,
    "cantidad": 2
  }'
🔨 Stack Tecnológico
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
Error: Service Unavailable en Render
Solución:

Render puede poner los servicios en sleep. Espera 30 segundos y reintenta

Verifica que la URL sea exacta

Revisa el status en el dashboard de Render

Error: CORS
Solución: Asegúrate que el header Authorization esté permitido en CORS

Error: Invalid JWT Token
Solución:

Token expirado: Usa refresh token

Token inválido: Verifica que incluya "Bearer " en el header

Usa el token correcto del microservicio de usuarios

📚 Documentación Adicional
Spring Boot Documentation

Render Deployment

JWT.io

Swagger/OpenAPI

👥 Contribuciones
Fork el proyecto

Crea una rama para tu feature

Commit tus cambios

Push a la rama

Abre un Pull Request

👤 Autores
Desarrollador Principal: Dante Muñoz

Institución: Duoc UC

Fecha: Diciembre 2025

Última actualización: Diciembre 14, 2025

