# syntax=docker/dockerfile:1

FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY src/ src/
RUN chmod +x mvnw && ./mvnw -B -DskipTests package

FROM tomcat:9.0-jdk17-temurin AS runtime
WORKDIR /usr/local/tomcat
RUN rm -rf webapps/*
COPY --from=build /workspace/target/appjsf-0.0.1-SNAPSHOT.war webapps/
EXPOSE 8080
CMD ["startup","run"]
