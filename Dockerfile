# --- Etapa 1: Construcción (Build) ---
# Usamos una imagen de Maven con Java 17 para compilar
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copiamos todo el código del proyecto al contenedor
COPY . .

# Compilamos el proyecto desde la raíz (esto generará los jars de todos los módulos)
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución (Run) ---
# Usamos una imagen ligera de Java 17 solo para correr la app
FROM openjdk:17-jdk-slim
WORKDIR /app

# COPIAMOS solo el JAR del microservicio que queremos arrancar (ms-usuario)
# OJO: Asegúrate de que la ruta coincida con tu carpeta real
COPY --from=build /app/ms-usuario/target/*.jar app.jar

# Exponemos el puerto (Render usa el 8080 por defecto internamente)
EXPOSE 8080

# Comando para iniciar la app
ENTRYPOINT ["java", "-jar", "app.jar"]