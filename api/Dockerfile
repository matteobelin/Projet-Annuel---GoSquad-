FROM gradle:jdk21 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src
COPY .env .env

RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app/.env .env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
