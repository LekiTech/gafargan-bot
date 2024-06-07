# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
# Assuming your library dependencies are part of your Maven project
RUN mvn -f /home/app/pom.xml clean install

# Database stage
FROM postgres:latest AS database
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=password
ENV POSTGRES_DB=gafargan_bot_db
EXPOSE 5433

# Application stage
FROM openjdk:17-slim
COPY --from=build /home/app/target/GafarganBot-jar-with-dependencies.jar /app/GafarganBot-jar-with-dependencies.jar
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "GafarganBot-jar-with-dependencies.jar"]

# Command to start
# docker build -t gafargan-bot:latest .
# docker run -d -e TELEGRAM_API_TOKEN=<API-TOKEN> --name gafargan-bot-test gafargan-bot:latest