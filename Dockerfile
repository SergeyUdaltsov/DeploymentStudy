FROM eclipse-temurin:21-jre

EXPOSE 8080

WORKDIR /app/

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=${ENV}","-jar","app.jar"]