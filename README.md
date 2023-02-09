# SherColorWeb
<!-- TOC -->
* [SherColorWeb](#shercolorweb)
  * [Description](#description)
    * [What does this application do?](#what-does-this-application-do)
    * [Technologies](#technologies)
  * [Business Owner](#business-owner)
  * [Where is it running?](#where-is-it-running)
  * [Prerequisites](#prerequisites)
  * [Install & Usage](#install--usage)
    * [Standalone Bootable Jar](#standalone-bootable-jar)
    * [Docker](#docker)
    * [Kubernetes](#kubernetes)
      * [Helm Chart](#helm-chart)
        * [Local](#local)
        * [Rancher](#rancher)
        * [Stores (k3s distribution)](#stores--k3s-distribution-)
  * [Testing](#testing)
  * [Contributing](#contributing)
<!-- TOC -->

## Description
Our signature SherColor web application
### What does this application do?
- Formulate SW and competitive colors
- Formulate via color match using a ColorEye.
- Dispense a formula using an automated dispenser
- Save jobs for future dispensing
- Maintain tint queue
- Print labels
- Performs functions to run the drawdown center
### Technologies
- Struts 2
- Spring Framework
- Hibernate
- JBoss EAP 7.0
- Helm
- Kubernetes
- Docker
- Vault (Secrets Management)
## Business Owner
TAG Color Excellence
## Where is it running?
- SW Application Servers
- Rancher
- SW Stores
## Prerequisites
- SherColorCommon
- SherColorLogin
- SherColorMath
- SWDeviceHandler

## Install & Usage
### Standalone Bootable Jar
1. Build the bootable jar using the following Maven command:

    ```sh
    mvn package
    ```
2. Run the bootable jar just as you would any other Jar:
    ```sh
    java -jar ./target/SherColorWeb-bootable.jar
    ```
For documentation and more visit https://docs.wildfly.org/bootablejar 

### Wildfly Bootable Jar Maven Plugin
Check out these maven commands for running locally:  
https://docs.wildfly.org/bootablejar/#_maven_plugin

#### Debugging
Prior to build the jar, uncomment the following configuration in the `wildfly-jar-maven-plugin`:
 ```xml
  <plugin>
     <groupId>org.wildfly.plugins</groupId>
     <artifactId>wildfly-jar-maven-plugin</artifactId>
     <version>7.0.2.Final</version>
     <configuration>
         ...
         <!-- Only enable the argument below for remote debugging purposes. These should always be commented
          out when committing to source control. -->
         <!-- <jvmArguments>
             <argument>-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005</argument>
         </jvmArguments> -->
     </configuration>
     <executions>
         <execution>
             <goals>
                 <goal>package</goal>
             </goals>
         </execution>
     </executions>
 </plugin>
 ```
 
Once the application is running, attach a remote debugger to the process using your IDE or command line (https://www.baeldung.com/java-application-remote-debugging)

### Docker
1. From the root directory of this project, you can use the following command to build the `shercolorweb` Docker image:
    ```sh
    docker build -t docker.artifactory.sherwin.com/sherwin-williams-co/colorex-shercolorweb:<tag> .
    ```

2. Run the image locally using the following command:
    ```sh
    docker run --env-file $HOME/dev.properties -p 8090:8090 -t docker.artifactory.sherwin.com/sherwin-williams-co/colorex-shercolorweb:<tag> <env>
    ```
    This command does a few important things:
    1. Copies the values in the dev.properties file as environment variables into the container which pass credential keys to Spring Boot used to authenticate to Vault
    2. Maps our local port to the container port
    3. Specifies the Wildfly application server environment to use which connects us to the proper Sherlink DB for user authentication

    Example:
    ```sh
    docker run --env-file $HOME/dev.properties -p 8090:8090 -t docker.artifactory.sherwin.com/sherwin-williams-co/colorex-shercolorweb:2.0.0 dev
    ```
### Kubernetes
Be sure to configure your local Kubernetes/Docker environment by using Docker Desktop or another Kubernetes distribution as well as Helm 3 installed.

#### Helm Chart

From the `shercolorweb-chart` directory of this project, you can use the following command to install the `shercolorweb` Helm chart:
##### Local
1. Download `secret-colorex-props.yaml` from Teams via **SherColor > Java Tools > Files** and apply it to your cluster via `kubectl apply`. This creates a local Kubernetes secret that contains the keys necessary to authenticate to Vault locally.
2. 
    ```sh
    helm install -f values.yaml -f values-local.yaml shercolorweb .
    ```
##### Rancher
```sh
helm install -f values.yaml -f values-rancher-<env>.yaml shercolorweb
```
##### Stores
You must pass the sherlink DB url in this command in order to keep this secret out of source control
```sh
helm install -f values.yaml -f values-stores.yaml --set sherlinkDbPassword=<value> shercolorweb
```
In order to apply updates to the release (running Helm chart), simply update the chart's configuration and then use the following command to execute a rolling update:
```sh
helm upgrade --set store.enabled=<store.enabled> shercolorweb ./shercolorweb-chart
```

## Testing
This application is tested in a number of ways:
1. JUnit tests which include various unit and integration tests at the DAO and Service layers.
2. Selenium-based browser automation tests that are currently created and run using [Katalon Recorder](https://www.katalon.com/katalon-recorder-ide/).
3. Manual device-based testing performed by the SherColor QA/Support team.

## Contributing
Please follow the standards outlined here --> [SherColor Coding Standards](https://swcompany.sharepoint.com/:u:/s/SherColor/EaJ93isLmexBtO0HDeVBuXcBwQ38ia_C7svG2nv3x19Wlg?e=uZZi3A)
