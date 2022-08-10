#FROM maven:3-openjdk-8-slim as builder
#COPY pom.xml .
#RUN mvn -B dependency:resolve-plugins dependency:go-offline
#COPY wildfly wildfly
#COPY src src
#RUN mvn -B package -DskipTests

#FROM openjdk:8-jre-slim
#LABEL maintainer="SherColor Team"
#EXPOSE 8090 8543
#COPY --from=builder target/SherColorWeb-bootable.jar SherColorWeb-bootable.jar
#ENTRYPOINT ["java","-jar","SherColorWeb-bootable.jar"]

# Uncomment this section to simply build an image from a jar pre-built with 'mvn package'
FROM openjdk:8-jre-slim
LABEL maintainer="SherColor Team"
EXPOSE 8090 8543
COPY target/SherColorWeb-bootable.jar SherColorWeb-bootable.jar
ENTRYPOINT ["java","-jar","SherColorWeb-bootable.jar"]