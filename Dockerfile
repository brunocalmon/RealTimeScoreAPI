FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn -B package --file pom.xml

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

COPY --from=builder /app/target/ScoreAPI-*.jar /app/scoreapi.jar

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "/app/scoreapi.jar"]
