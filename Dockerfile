# Build stage

FROM maven:3.8.4-openjdk-17-slim AS build
COPY src /home/app/src
COPY audio /home/app/audio
COPY alphabet /home/app/alphabet
COPY lib /home/app/lib
COPY pom.xml /home/app
RUN mvn install:install-file -Dfile="/home/app/lib/lezgi-numbers-java-0.0.1.jar" -DgroupId="io.lekitech" -DartifactId=lezgi-numbers-java -Dversion="1.0-SNAPSHOT" -Dpackaging=jar
RUN mvn -f /home/app/pom.xml clean package

# Package stage

FROM openjdk:17-slim
COPY --from=build /home/app/target/GafarganBot-jar-with-dependencies.jar ./GafarganBot-jar-with-dependencies.jar
COPY --from=build /home/app/audio ./audio
COPY --from=build /home/app/alphabet ./alphabet
EXPOSE 8080
ENTRYPOINT ["java","-jar","./GafarganBot-jar-with-dependencies.jar"]

# Command to start
# docker build -t gafargan-bot:latest .
# docker run -e TELEGRAM_API_TOKEN=<API-TOKEN> --name gafargan-bot gafargan-bot:latest