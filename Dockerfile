# Use a imagem oficial do OpenJDK como base
FROM openjdk:11-jdk-slim

# Defina o diretório de trabalho no container
WORKDIR /app

# Copie o arquivo JAR do seu aplicativo para o container
COPY build/libs/com.contas.padarias-0.0.1.jar /app/app.jar

# Exponha a porta que seu aplicativo Ktor usa
EXPOSE 8080

# Comando para executar o aplicativo
CMD ["java", "-jar", "/app/app.jar"]