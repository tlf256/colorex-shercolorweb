FROM openjdk:11-jre-slim
LABEL maintainer="SherColor Team"
EXPOSE 8090 8543
COPY target/SherColorWeb-bootable.jar SherColorWeb-bootable.jar
ADD docs /web_apps/server/shercolor/external
ENTRYPOINT ["java","-jar","SherColorWeb-bootable.jar"]