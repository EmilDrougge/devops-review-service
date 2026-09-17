FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Resolve to a single runnable jar, excluding Spring Boot's "-plain.jar"
# (and legacy ".original") so the next COPY is never ambiguous.
RUN JAR_FILE=$(find target -maxdepth 1 -name '*.jar' \
        -not -name '*-plain.jar' -not -name '*.original' | head -1) \
    && cp "$JAR_FILE" /app/app.jar

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/app.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]