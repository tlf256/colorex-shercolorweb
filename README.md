# SherColorWeb
## Description
Our signature SherColor application
### What does this application do?
-Formulate SW and competitive colors
-Formulate via color match using a ColorEye.
-Dispense a formula using an automated dispenser
-Save jobs for future dispensing
-Maintain tint queue
-Print labels
-Performs functions to run the drawdown center
### Technologies
- Struts 2
- Spring Framework
- Hibernate
- JBoss EAP 7.0
- Helm
- Kubernetes
- Docker
## Business Owner
//TAG Color Excellence
## Where is it running?
//SW application servers
## Prerequisites
- SherColorCommon
- SherColorLogin
- SWDeviceHandler
- SherColorMath
- *For running in Kubernetes*
    - Kubernetes
    - Helm 3
    - Docker

## Install & Usage
### Bootable Jar
1. Build the bootable jar using the following Maven command:

    ```sh
    mvn package
    ```
2. Run the bootable jar just as you would any other Jar:
    ```sh
    java -jar ./target/SherColorWeb-bootable.jar
    ```

### Kubernetes
Be sure to configure your local Kubernetes/Docker environment by using Docker Desktop or another Kubernetes distribution as well as Helm 3 installed.
#### Docker Image
1. From the root directory of this project, you can use the following command to install the `shercolorweb` Docker image:
    ```sh
    docker build -t shercolorweb .
    ```
2. Run the image using the following command:
    ```sh
    docker run -p 8090:8090 shercolorweb
    ```
#### Helm Chart
TODO:
- applying different yaml files
- passing in the oracle shercolor db password in install command when running centralized instance
- for stores pass stores yaml and set admin password, for rancher pass rancher yaml and set oracle password

From the root directory of this project, you can use the following command to install the `shercolorweb` Helm chart:
```sh
helm install --set store.enabled=<store.enabled> shercolorweb ./shercolorweb-chart
```
In order to apply updates to the release (running Helm chart), simply update the chart's configuration and then use the following command to execute a rolling update:
```sh
helm upgrade --set store.enabled=<store.enabled> shercolorweb ./shercolorweb-chart
```
##### Conditions
`store.enabled` - Set to true if running in a store, defaults to false otherwise. This will enable all configuration necessary to run SherColorWeb in a store Kubernetes environment.

## Testing
This application is tested in a number of ways:
1. JUnit tests which include various unit and integration tests at the DAO and Service layers.
2. Selenium-based browser automation tests that are currently created and run using [Katalon Recorder](https://www.katalon.com/katalon-recorder-ide/).
3. Manual device-based testing performed by the SherColor QA/Support team.

## Contributing
Please follow the standards outlined here --> [SherColor Coding Standards](https://swcompany.sharepoint.com/:u:/s/SherColor/EaJ93isLmexBtO0HDeVBuXcBwQ38ia_C7svG2nv3x19Wlg?e=uZZi3A)
