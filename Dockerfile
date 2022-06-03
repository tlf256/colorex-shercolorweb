FROM maven:3-alpine as builder
COPY src .
COPY pom.xml .
COPY wildfly .
RUN ["mvn","clean","package","-DskipTests"]

FROM openjdk:8u332-jre-slim
LABEL maintainer="SherColor Team"
EXPOSE 8090 8543
COPY --from=builder target/SherColorWeb-bootable.jar SherColorWeb-bootable.jar
ADD docs /web_apps/server/shercolor/external
ENTRYPOINT ["java","-jar","SherColorWeb-bootable.jar"]