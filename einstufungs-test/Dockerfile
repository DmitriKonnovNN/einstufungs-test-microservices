FROM eclipse-temurin:11-alpine@sha256:102d38b35b2404d379b1c2d36b4f2c6c8de7370faac2803044a9f3fd1fc5f875
RUN mkdir greeting;
WORKDIR greeting
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]