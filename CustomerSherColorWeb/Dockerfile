FROM openjdk:8
EXPOSE 8090
ADD target/SherColorWeb-bootable.jar SherColorWeb-bootable.jar
COPY shercolorcommonnew.properties /web_apps/server/shercolor/deploy/shercolorcommonnew.properties
ENTRYPOINT ["java","-jar","SherColorWeb-bootable.jar"]
