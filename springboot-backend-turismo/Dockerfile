# Usar una imagen base con JDK 11 y Maven
FROM maven:3.8.4-openjdk-17 AS build

# Establecer un directorio de trabajo
WORKDIR /app

# Copiar archivos de tu proyecto al directorio de trabajo
COPY . /app

# Ejecutar Maven para construir el proyecto
RUN mvn clean package -DskipTests

# Crear una nueva imagen basada en OpenJDK 11
FROM openjdk:17-jdk-slim AS runtime

# Exponer el puerto que utilizará la aplicación
EXPOSE 8080

# Copiar el archivo JAR construido desde la etapa anterior
COPY --from=build /app/target/test.assigment.backend..turismo-0.0.1-SNAPSHOT.jar /app/test.assigment.backend..turismo-0.0.1-SNAPSHOT.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/test.assigment.backend..turismo-0.0.1-SNAPSHOT.jar"]
