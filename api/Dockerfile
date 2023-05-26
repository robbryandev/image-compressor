FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /app

COPY . .

RUN mvn package

WORKDIR /app/target

EXPOSE 8080

CMD [ "java", "-jar", "./compress-0.0.1-SNAPSHOT.jar" ]