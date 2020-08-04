#FROM openjdk:8-jdk-alpine
FROM adoptopenjdk/maven-openjdk8:latest

MAINTAINER João Amaral

COPY pom.xml /usr/local/service/pom.xml
COPY src /usr/local/service/src

WORKDIR /usr/local/service

ENV group unit

RUN mvn clean install -Dgroups={group}

CMD ["java", "-jar", "target/sorting-challenge-jar-with-dependencies.jar"]
