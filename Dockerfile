# Estágio de build
FROM gradle:8.8.0-jdk17 AS build

# Defina o diretório de trabalho no container
WORKDIR /home/gradle/src

# Copie apenas os arquivos necessários para o cache de dependências
COPY settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle ./gradle

# Cache das dependências do Gradle
RUN gradle dependencies --no-daemon

# Copie o restante do código-fonte
COPY src ./src

# Execute o build do Gradle
RUN gradle build -x test --no-daemon

# Estágio de produção
FROM openjdk:17-slim

# Defina o diretório de trabalho no container
WORKDIR /app

# Copie o arquivo JAR do estágio de build
COPY --from=build /home/gradle/src/build/libs/*-all.jar ./app.jar

# Exponha a porta que seu aplicativo Ktor usa
EXPOSE 8080

# Comando para executar o aplicativo
CMD ["java", "-jar", "/app/app.jar"]