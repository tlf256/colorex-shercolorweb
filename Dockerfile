FROM openjdk:11
LABEL maintainer="SherColor Team"
EXPOSE 8090
ADD target/SherColorWeb-bootable.jar SherColorWeb-bootable.jar
ENTRYPOINT ["java","-jar","SherColorWeb-bootable.jar"]
 