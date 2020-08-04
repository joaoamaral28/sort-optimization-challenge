#FROM openjdk:8-jdk-alpine
FROM adoptopenjdk/maven-openjdk8:latest

MAINTAINER João Amaral

COPY pom.xml /usr/local/service/pom.xml
COPY src /usr/local/service/src

WORKDIR /usr/local/service

RUN mvn clean install -Dgroups=unit

CMD ["java", "-jar", "target/sorting-challenge-jar-with-dependencies.jar"]