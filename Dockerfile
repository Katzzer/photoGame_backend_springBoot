FROM openjdk:17-alpine
#RUN mvn clean install
ARG JAR_FILE=api/target/*.jar
COPY ${JAR_FILE} api-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/api-1.0-SNAPSHOT.jar"]