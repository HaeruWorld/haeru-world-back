FROM openjdk:11-jre-slim as builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:11-jre-slim
COPY --from=builder build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=gcp","/app.jar"]