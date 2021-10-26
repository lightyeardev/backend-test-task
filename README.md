# backend-test-task
Test task for backend interviews

## Getting Started

Before running the application please ensure you have docker installed:
* Install Docker: https://docs.docker.com/get-docker/
* Install Docker Compose: https://docs.docker.com/compose/install/
* *For Windows Only*: You need to set the `DOCKER_HOST` environment variable to `npipe:////./pipe/docker_engine`

Then:

* Start Postgres: `docker-compose -f docker/docker-compose.yml up`
* Start Application: `./gradlew clean bootRun`
* Open http://localhost:8080/account/

## Testing
All tests (unit and integration) share the same test folder `src/test/groovy`.

### Unit tests
* All unit tests should end with `Spec`, e.g. `AccountServiceSpec.groovy`.
* Unit tests should extend `spock.lang.Specification`

### Integration tests
* All integration tests should end with `IntSpec`, e.g. `AccountControllerIntSpec.groovy`
* Integration tests should extend `com.golightyear.backend.AbstractIntegrationSpec`

Run tests using `./gradlew test`

## Issues
* If you run into any issues please do create a ticket here: https://github.com/lightyeardev/backend-test-task/issues
* Also, please do feel free to reach out to recruitment person! We want you to succeed & will help you however we can!