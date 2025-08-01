FROM eclipse-temurin:24-jdk AS build
WORKDIR /app

COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle ./gradle

RUN apt-get update \
    && apt-get install -y --no-install-recommends dos2unix \
    && dos2unix gradlew

RUN chmod +x gradlew \
    && ./gradlew --no-daemon dependencies

COPY src ./src
RUN ./gradlew --no-daemon clean bootJar

FROM eclipse-temurin:24-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
