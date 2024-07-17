FROM openjdk:17-jdk as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /
RUN java -Djarmode=layertools -jar gateway-app.jar extract
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]
