# Products Management Application

This is a Spring Boot application for managing products. It provides easy-to-use REST APIs to create, list, and update products.

### Prerequisites

- Java 11 or higher
- PostgreSQL database
- Docker (for running the application locally with Docker)
- Docker Compose (for running the application locally with Docker Compose)

## Steps to run

### Run database locally

1. To run the application locally using Docker, follow these steps:

    - Ensure Docker is installed and running on your machine.
    -  Ensure Docker Compose is installed.

    To start the application with Docker Compose, run the following command:

    ```sh
    docker-compose up -d
    ```

    This will start the application along with any required services defined in the docker-compose.yml file.
2. Ensure correct datasource connection values in application.properties    

### Build and Test

To build and test the application, run the following command:

```sh
./gradlew build
```

### Run the application

```sh
./gradlew bootRun
```
We are using liquibase which will prepoluate the database with some initial values.

## OpenAPI Documentation
The OpenAPI documentation is available at the following URL:
```
http://localhost:9090/swagger-ui.html
```