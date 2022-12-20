FROM openjdk:17-alpine
ARG JAR_FILE=api/target/*.jar
COPY ${JAR_FILE} api.jar
ENTRYPOINT ["java","-jar","/api.jar"]