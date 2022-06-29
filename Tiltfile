# Build the image
docker_build('shercolorweb-dev', '.',
  dockerfile='./Dockerfile_dev',
  live_update=[sync('./src/', '/src'),
               sync('./wildfly/', '/wildfly'), 
               sync('./src/pom.xml', '/pom.xml') ],
  entrypoint = 'mvn -B wildfly-jar:dev-watch')

# Tell Tilt to watch specific files for live reload


# Compile the helm chart templates and register the resource
k8s_yaml(helm('shercolorweb-chart'
    ,name='shercolorweb'
    ,values=['./shercolorweb-chart/values.yaml','./shercolorweb-chart/values-local.yaml']))
k8s_resource('shercolorweb', port_forwards='8090:8090')