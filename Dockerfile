FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
RUN mvn --batch-mode --no-transfer-progress package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/details-0.0.1-SNAPSHOT.war app.war
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -jar app.war --server.port=${PORT:-8080}"]
