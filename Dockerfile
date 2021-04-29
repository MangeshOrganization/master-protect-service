FROM openjdk:11.0.11-jdk
RUN adduser java
RUN adduser java java
USER java:java
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
