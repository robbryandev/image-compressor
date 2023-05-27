FROM maven:3.9.2-eclipse-temurin-17-alpine

COPY . /tmp/compress

WORKDIR /tmp/compress

RUN apk add ffmpeg
RUN mvn package

WORKDIR /app

RUN cp /tmp/compress/target/compress-0.0.1-SNAPSHOT.jar /app/compress.jar
COPY ./src/main/resources/application.properties /app/config/application.properties
COPY images/ /app/images/

EXPOSE 8080

CMD [ "java", "-jar", "./compress.jar" ]