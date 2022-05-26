FROM docker.artifctory.sherwin.com/openjdk:8
LABEL maintainer="SherColor Team"
EXPOSE 8090 8543
ADD target/SherColorWeb-bootable.jar SherColorWeb-bootable.jar
ENTRYPOINT ["java","-jar","SherColorWeb-bootable.jar"]