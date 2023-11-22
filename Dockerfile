
FROM gradle:8.1.1-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src/
WORKDIR /home/gradle/src

RUN gradle build --no-daemon
RUN ls **/*

FROM openjdk:17.0.1-jdk-slim

EXPOSE 8080

RUN mkdir /app


COPY --from=build /home/gradle/src/build/libs/demo-0.0.1-SNAPSHOT.jar ./app/spring-boot-application.jar
RUN apt-get update -qq && apt-get install ffmpeg -y
ENTRYPOINT ["ffplay"]
ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]
