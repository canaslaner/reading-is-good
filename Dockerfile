FROM adoptopenjdk/openjdk11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} reading-is-good.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/reading-is-good.jar"]