FROM maven:3.9-eclipse-temurin-17 AS compilacion
WORKDIR /proyecto

COPY pom.xml .
COPY src/ ./src/
RUN mvn -B package

FROM tomcat:10.1-jdk17-temurin
COPY --from=compilacion /proyecto/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war