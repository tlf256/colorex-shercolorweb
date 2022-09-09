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
- Maintains a tint queue  
- Print labels  
- Perform functions to run the drawdown center  
### Technologies
- Struts 2
- Spring Framework
- Hibernate
- JBoss EAP 7.0
- Helm
- Kubernetes
- Docker
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
For debugging documentation and more visit https://docs.wildfly.org/bootablejar 

### Docker
1. From the root directory of this project, you can use the following command to install the `shercolorweb` Docker image:
    ```sh
    docker build -t docker.artifactory.sherwin.com/sherwin-williams-co/colorex-shercolorweb:<version> .
    ```
    > **Note:** If running locally pass the `--build-arg local=true` argument to the end of this command to copy the vault-specific dev.properties into the image

2. Run the image using the following command:
    ```sh
    docker run -p 8090:8090 docker.artifactory.sherwin.com/sherwin-williams-co/colorex-shercolorweb:<version>
    ```
### Kubernetes
Be sure to configure your local Kubernetes/Docker environment by using Docker Desktop or another Kubernetes distribution as well as Helm 3 installed.

#### Helm Chart

From the `shercolorweb-chart` directory of this project, you can use the following command to install the `shercolorweb` Helm chart:
##### Local
You must pass the sherlink DB url in this command in order to keep this secret out of source control
```sh
helm install -f values.yaml -f values-local.yaml --set sherlinkDbUrl=<value> shercolorweb
```
##### Rancher
```sh
helm install -f values.yaml -f values-rancher.yaml shercolorweb
```
##### Stores (k3s distribution)
You must pass the sherlink DB url in this command in order to keep this secret out of source control
```sh
helm install -f values.yaml -f values-stores.yaml --set sherlinkDbUrl=<value> shercolorweb
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
