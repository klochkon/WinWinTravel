FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/data-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]