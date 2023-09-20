# springboot 3 kafka demo

### overview
This demo repository contains a springboot 3 maven project which

* exposes a Spring MVC REST API to maintain a 'user' object
* sends 'user' data received over the /kafka/user API endpoint to a Kafka topic
* data consumed from the Kafka topic are then saved to an in-memory version of an H2 database, using
  * JPA annotations
  * Spring Data
* data added in this manner can then be queried via the REST API
* metrics exposed by the micrometer libraries are collected by a configured prometheus DB  
* exposes swagger/open-api interface information - once started, see [here](http://localhost:8080/swagger-ui.html)

When started using docker-compose (see below for details) this demo provides a platform where
* new user details are published in JSON format to kafka via REST endpoint /kafka.user
* those details are consumed by a Kafka listener and 
  * converted from JSON to a User DTO format
  * added to the in-memory H2 database
* If the user cannot be added to H2 because for instance the email address is not unique
  * the data will be routed to a 'dead letter topic'
    * consumed by a dead letter listener and logged

It is a simple composition of the relevant docker containers that provide publish/consume on Kafka, REST/JPA interaction, and 'observability' via prometheus and grafana.

### development environment: <a name="environment"></a>
This code was developed and tested on:
```agsl
* Linux 5.15.0-71-generic 78-Ubuntu x86_64 GNU/Linux
* OpenJDK Runtime Environment (build 17.0.6+10-Ubuntu-0ubuntu122.04)
```

### docker-compose
The docker-compose.yml file contains configuration for
* starting a zookeeper docker container (which acts as a broker for kafka requests)
* starting a kafka container to publish/consume requests
* starting a prometheus DB docker container
* starting a grafana docker container which is configured to 
  * expose metrics for collection by prometheus
  * expose a default dashboard of metrics from the demo springboot application
* starting the demo springboot project

It will start all containers locally. The springboot app contains default connectivity to grafana

The grafana dashboard can be viewed, once started, see [here](http://localhost:3000) - login with admin/admin

### to see the demo ......
* you'll need docker/docker-compose installed locally - you'll have to check that out yourself ..
* clone this repository to your local disk
* build the springboot demo. If you want to do this yourself, you'll need java 17 installed, and if not using the maven wrapper mvnw mentioned below, a local install of maven
  * on the command line in the root directory (i.e. the same directory as the 'pom.xml' file), type
```agsl
./mvnw clean install
```
* once the build finishes, there should be a file 'spring-boot-kafka-demo-0.0.1-SNAPSHOT.jar' visible in the target directory
* on the command line in the root directory (i.e. the same directory as the 'docker-compose.yml' file), type
```agsl
docker-compose build
```
* this instruction prepares the docker containers necessary to run the demo
* when this instruction is complete, type:
```agsl
docker-compose up
```
* this should start all of the necessary containers

Once all containers are running, you can proceed to add some data over the demo REST API
* click [here](http://localhost:8080/swagger-ui.html) to access and use the Open API documentation to add some data 
  * Add some data:
    * Find the POST /kafka/user option and select 'Try it out'
    * Click on the 'Execute' button a few times
  * Query the data you've just added
    * Find the GET /users option and click 'Try it out'
    * Click on the 'Execute' button a few times
  * NOTE that adding user details with the email attribute unchanged will trigger a dead letter topic action visible in th elogs, as the H2 database requires that the email attribute for each user is unique.
* This activity will now be visible in grafana, having been stored in the prometheus DB
  * click [here](http://localhost:3000), login (admin/admin) and then find and click on the springboot-demo dashboard
* Err ... that's it

### tests and code coverage
The project is also configured to produce code coverage data using the jacoco maven plugin.
After a build, this information can be found here: target/site/jacoco/index.html

### Sonar
Sonar can be built stand-alone as detailed below if you have access to an instance.
NB You'll need to update the 'sonar' properties in the pom.xml file to identify the host and login token to be used when sending analysis to sonar.

```$xslt
mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install
mvn sonar:sonar
```

### Prometheus/Grafana
Unlike InfluxDB, to which data is sent by the micrometer libraries, prometheus by default scrapes metrics from a page exposed (in this case at http://localhost:8080/actuator/prometheus) by the micrometer libraries.
Grafana is configured to use prometheus as a datasource and display those metrics in a graphic form.
Note that in grafana we are making use of a default dashboard made available at:
```agsl
https://grafana.com/grafana/dashboards/4701%22
```
prometheus configuration can be checked once the container is up, at:
```agsl
http://localhost:9090/targets?search=
```
Individual metrics can be viewed directly in prometheus, for instance:
```agsl
http://localhost:9090/graph?g0.expr=jvm_classes_loaded_classes&g0.tab=0&g0.stacked=0&g0.show_exemplars=0&g0.range_input=1h
```
## Example Grafana metrics after REST API activity
![Image](spring-boot-kafka-demo.png)