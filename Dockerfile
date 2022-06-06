FROM maven:3-openjdk-8 as builder
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src .
COPY wildfly .
RUN mvn -B clean package -DskipTests

FROM openjdk:8u332-jre-slim
LABEL maintainer="SherColor Team"
EXPOSE 8090 8543
COPY --from=builder target/SherColorWeb-bootable.jar SherColorWeb-bootable.jar
ADD docs /web_apps/server/shercolor/external
ENTRYPOINT ["java","-jar","SherColorWeb-bootable.jar"]