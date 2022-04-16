FROM openjdk:11.0.1-jdk
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/students-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} students-app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/students-app.jar"]
