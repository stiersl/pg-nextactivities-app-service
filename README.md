# pg-nextactivities-app-service

Backend For Frontend (BFF) service to support gathering data for P&Gs nextactivities-app UI app .   This service fetches data from the core-domain services as specified in dependencies section below.


##### Building and Running the Service
Build with: `./gradlew clean build`

Run with: `./gradlew bootRun`

> **NOTE:** The default pg-nextactivities-app-service URL is: http://localhost:8093/
---
### Logging

The location of the service log is configured using the `logging.path` property in src/main/resources/application.properties.
The suggested location on Windows is `C:\ProgramData\GE Digital\logs`.


  
---
### Docker Container
The pg-nextactivities-app-service's docker image can be build using the following commands:

> `docker build -t pg-nextactivities-app-service:0.4.4 --build-arg VERSION=0.4.4 --build-arg JAR_FILE=build/libs/pg-nextactivities-app-service-0.4.4.war .`

This will build docker image. Modify the version number in the tag as needed.

> `docker run -d --name pg-nextactivities-app-service -p 8093:8093 pg-nextactivities-app-service:0.4.4`

This will create and start a docker container with the name 'pg-nextactivities-app-service', mapped to port '8093' on the local machine.
The service can then be accessed through 'http://localhost:8093/'.

The next activities app service can be configured with the following environment variables (passed into the docker container using argument -e during docker run) along with other core service dependencies:
> `server_port="8093"`
> `uaa_service_origin="https://path/to/uaa"`
