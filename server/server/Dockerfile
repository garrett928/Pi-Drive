FROM eclipse-temurin:23.0.1_11-jre-noble

WORKDIR /app

CMD ["./gradlew", "clean", "bootJar"]
COPY build/libs/*.jar app.jar
COPY src ./src

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
