# reservisland

This service provides a REST API for managing a campsite reservations.

---
### Requirements and Tools
* [Java](https://openjdk.java.net/projects/jdk/11/) 11
* [Gradle](https://gradle.org/) 7.2
* Enconding: **UTF-8**
* [Spring Boot 2](http://spring.io/projects/spring-boot)
* [Lombok](https://projectlombok.org/)
* [H2 Database](https://www.h2database.com)

#### Build
```sh 
./gradlew clean build 
```
#### Run
```sh 
./gradlew bootRun
```
---
### Service Usage
With the service running, you can access the Swagger UI to use the API and the H2 Console to manage the Database. 
* [Swagger UI](http://localhost:8099/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)
* [H2 Console](http://localhost:8099/h2-console)
---
### Architecture
The service is stateless, which means it can be scaled horizontally as long as the database is able to handle connections.

![Alt text](images/architecture-v1.png?raw=true "Architecture V1")

