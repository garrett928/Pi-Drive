FROM eclipse-temurin:23.0.1_11-jre-noble

WORKDIR /app

CMD ["./gradlew.bat", "clean", "bootJar"]
COPY src ./src
COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
