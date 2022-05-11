FROM bitnami/wildfly:24.0.1
ARG ENV
LABEL maintainer="SherColor Team"
EXPOSE 8090
ENV WILDFLY_USERNAME=admin
ENV WILDFLY_PASSWORD=12345678
ADD target/SherColorWeb-*.war /opt/bitnami/wildfly/standalone/
ADD wildfly/standalone/$ENV/standalone.xml /bitnami/wildfly/configuration/
ADD wildfly/includes/standalone/configuration/server.keystore /bitnami/wildfly/configuration/
ADD wildfly/includes/modules/com/sherwin/shercolor/swinternetrestrictorhandler/main /opt/bitnami/wildfly/modules/com/sherwin/shercolor/swinternetrestrictorhandler/main/
ADD wildfly/includes/modules/oracle/jdbc/main /opt/bitnami/wildfly/modules/oracle/jdbc/main/
ADD wildfly/includes/modules/system/layers/base/javaee/api/main /opt/bitnami/wildfly/modules/system/layers/base/javaee/api/main/