#Simply build an image from a jar pre-built with 'mvn package'
FROM openjdk:8-jre-slim
LABEL maintainer="SherColor Team"
ENV GC_METASPACE_SIZE=96
EXPOSE 8090 8543
COPY wildfly wildfly
COPY target/SherColorWeb-bootable.jar SherColorWeb-bootable.jar
RUN echo '#!/bin/bash \n java -jar SherColorWeb-bootable.jar --cli-script=wildfly/scripts/setup.cli -Djboss.bind.address=0.0.0.0 -Djboss.bind.address.management=0.0.0.0 --properties=wildfly/scripts/properties/"${1:-dev}".properties' > ./entrypoint.sh
RUN chmod +x ./entrypoint.sh
ENTRYPOINT ["./entrypoint.sh"]

#use this configuration to pull all dependencies and build via docker buildpacks
#FROM maven:3-jdk-8-slim as builder
#COPY pom.xml .
#COPY wildfly wildfly
#RUN mvn -B dependency:resolve-plugins dependency:go-offline
#COPY src src
#RUN mvn -B package -DskipTests
#
#FROM openjdk:8-jre-slim
#LABEL maintainer="SherColor Team"
#EXPOSE 8090 8543
##Recommended by Wildfly docs
#ENV GC_METASPACE_SIZE=96
#COPY wildfly wildfly
#COPY --from=builder target/SherColorWeb-bootable.jar SherColorWeb-bootable.jar
#RUN echo '#!/bin/bash \n java -jar SherColorWeb-bootable.jar --cli-script=wildfly/scripts/setup.cli --properties=wildfly/scripts/properties/"${1:-dev}".properties' > ./entrypoint.sh
#RUN chmod +x ./entrypoint.sh
#ENTRYPOINT ["./entrypoint.sh"]