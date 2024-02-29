FROM openjdk:21

LABEL author = "evgereo"

COPY target/articles-api-0.0.1-SNAPSHOT.jar /app/app.jar
COPY src/main/resources/db /app/db/migration

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
