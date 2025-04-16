# Build Stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon
COPY src ./src
RUN ./gradlew clean build --no-daemon

# Production Stage
FROM eclipse-temurin:21-jre-ubi9-minimal AS production
ENV APP_BASE /opt/app
RUN mkdir -p $APP_BASE
COPY --from=build /app/build/libs/*.jar $APP_BASE/app.jar
RUN ls -la $APP_BASE/
USER root
RUN groupadd -g 900 docker && useradd -m -u 901 -g docker -s /bin/bash docker
USER docker
EXPOSE 8080
CMD ["java", "-jar", "/opt/app/app.jar"]