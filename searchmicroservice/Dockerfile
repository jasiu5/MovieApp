FROM openjdk:17-alpine as builder
WORKDIR search-service
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} search-service.jar
RUN java -Djarmode=layertools -jar search-service.jar extract

FROM openjdk:17-alpine
WORKDIR search-service
COPY --from=builder search-service/dependencies/ ./
COPY --from=builder search-service/spring-boot-loader/ ./
COPY --from=builder search-service/snapshot-dependencies/ ./
COPY --from=builder search-service/application/ ./

EXPOSE 8083

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
