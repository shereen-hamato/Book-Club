FROM openjdk:8-jre
ADD target/ebookclub-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8187
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
