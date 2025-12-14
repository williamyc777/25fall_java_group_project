FROM openjdk:17
LABEL authors="Shinomiya"

EXPOSE 8082

COPY ./target/backend-0.0.1-SNAPSHOT.jar /usr/src/myapp/app.jar
COPY ./src/main/resources/images /usr/src/myapp/images
WORKDIR /usr/src/myapp
CMD ["java","-jar","app.jar"]

