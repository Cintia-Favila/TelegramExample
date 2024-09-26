FROM maven:latest AS Build

WORKDIR /app

ADD . .

RUN mvn install -DskipTests

FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY --from=Build /app/target/telegramExample-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=envs", "telegramExample-0.0.1-SNAPSHOT.jar"]
