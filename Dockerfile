FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn

COPY src src
RUN chmod +x mvnw && ./mvnw clean package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/aquasos-0.0.1-SNAPSHOT.jar app.jar

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

RUN chown -R appuser:appgroup /app
USER appuser:appgroup

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]