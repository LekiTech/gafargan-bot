# Build stage

FROM maven:3.8.4-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Package stage

FROM openjdk:17-slim
COPY --from=build /home/app/target/GafarganBot-1.0-SNAPSHOT.jar ./GafarganBot.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","./GafarganBot-jar-with-dependencies.jar"]

# Command to start
# docker build -t gafargan-bot:latest .
# docker run -d --name gafargan-bot gafargan-bot:latest