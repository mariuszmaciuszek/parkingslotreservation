FROM gradle:8.2.1 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM openjdk:17

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/parkingslotreservation-*SNAPSHOT.jar /app/parking-slot-reservation-service.jar

ENTRYPOINT ["java","-jar","/app/parking-slot-reservation-service.jar"]
