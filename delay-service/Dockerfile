FROM amazoncorretto:17-alpine-jdk
COPY target/delay-service-1.0.0-SNAPSHOT.jar delay-service.jar
ENTRYPOINT ["java","-jar","/delay-service.jar"]