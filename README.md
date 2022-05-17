# SherColorWeb
## Description
### What does this application do?
//TODO
### Technologies
- Struts 2
- Spring Framework
- Hibernate
- JBoss EAP 7.0
- Helm
- Kubernetes
- Docker
## Business Owner
//TODO
## Where is it running?
//TODO
## Prerequisites
- SherColorCommon
- SherColorLogin
- SWDeviceHandler
- SherColorMath
- *For running in Kubernetes*
    - Kubernetes
    - Helm 3
    - Docker
## Install Helm Chart
From the root directory of this project, you can use the following command to install the `shercolorweb` Helm chart:
```sh
helm install --set store.enabled=<store.enabled> shercolorweb ./shercolorweb-chart
```
### Conditions
`store.enabled` - Set to true if running in a store, defaults to false otherwise. This will enable all configuration necessary to run SherColorWeb in a store Kubernetes environment.
## Usage
//TODO
### Testing Instructions
This application is tested in a number of ways:
1. JUnit tests which include various unit and integration tests at the DAO and Service layers.
2. Selenium-based browser automation tests that are currently created and run using [Katalon Recorder](https://www.katalon.com/katalon-recorder-ide/).
3. Manual device-based testing performed by the SherColor QA/Support team.
## Contributing
Please follow the standards outlined here --> [SherColor Coding Standards](https://swcompany.sharepoint.com/:u:/s/SherColor/EaJ93isLmexBtO0HDeVBuXcBwQ38ia_C7svG2nv3x19Wlg?e=uZZi3A)


## Veracode Policy Scan Launch Unsuccessful :x:
            
Policy scan launch was unsuccessful, please review the workflow details in Actions tab to determine cause.

Feel free to attempt to re-trigger the scan using one of the two options:
1.  Add a label to the pull request titled `trigger-sast` (remove and re-add to re-trigger)
2.  Submit a new Approving Pull Request Review
>**Note**: A new approval is required when any additional non-merge commits are pushed to the pull request branch.