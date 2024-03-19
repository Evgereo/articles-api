# articles-api
The project is built on Spring Boot and is an interaction between users and articles. The application implements user authentication using JWT tokens and role-based authorization. In addition, the project includes unit tests for all controllers and services that have business logic. The application is connected to databases such as Redis and PostgreSQL. The project can be built and run using Docker Compose.

To run the api using docker compose, use the command "docker compose --profile deploy up --build" and all the required api containers will be started and configured independently of each other.

P.S don't forget to compile a new jar file (mvn package -DskipTest, tests don't work it ok) and set your variables in the .env file (check .env.example)
