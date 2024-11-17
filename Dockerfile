# Use uma imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o arquivo JAR da aplicação para o container
COPY build/libs/accountspayable-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que o Spring Boot usa (8080 por padrão)
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]