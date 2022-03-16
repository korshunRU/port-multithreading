FROM gradle:jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM openjdk:11.0-jdk-slim
RUN mkdir /app
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/pet-mthreading-port-1.0.jar .
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom", "-jar", "pet-mthreading-port-1.0.jar"]