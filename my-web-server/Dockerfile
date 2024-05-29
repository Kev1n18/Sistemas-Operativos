# Usar uma imagem base do Maven para construir o projeto
FROM maven:3.8.5-openjdk-17 AS build

# Definir o diretório de trabalho dentro do container
WORKDIR /app

# Copiar o arquivo pom.xml e os arquivos de dependência
COPY pom.xml .
COPY src ./src

# Executar o Maven para construir o projeto e criar o "fat JAR"
RUN mvn clean package -DskipTests

# Usar uma imagem base do JDK para executar o projeto
FROM openjdk:17-jdk-slim

# Definir o diretório de trabalho dentro do container
WORKDIR /app

# Copiar o arquivo "fat JAR" construído na fase anterior para a nova imagem
COPY --from=build /app/target/my-web-server-1.0-SNAPSHOT.jar /app/my-web-server.jar

# Copiar os arquivos de configuração necessários
COPY --from=build /app/src/main/java/resources/config/http.json /app/src/main/java/resources/config/http.json

# Copiar o diretório www para o contêiner
COPY src/main/www /app/src/main/www

# Expor a porta em que a aplicação irá rodar
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "my-web-server.jar"]