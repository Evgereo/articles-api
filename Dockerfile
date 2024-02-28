FROM openjdk:21

LABEL authors="evgereo"

COPY target/articles-api-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]