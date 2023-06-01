## Fetching latest version of Java
#FROM openjdk:17
#
## Setting up work directory
#WORKDIR /app
#
## Copy the jar file into our app
#COPY ./target/MovieService-0.0.1-SNAPSHOT.jar /app
#
## Exposing port 8080
#EXPOSE 8080
#
## Starting the application
#CMD ["java", "-jar", "MovieService-0.0.1-SNAPSHOT.jar"]

FROM openjdk:17-alpine as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:17-alpine
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

EXPOSE 8081

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]