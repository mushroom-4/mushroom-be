#BUILD stage
FROM amazoncorretto:17 AS builder
WORKDIR /build
COPY . .
RUN ./gradlew build -x test

#RUN stage
FROM amazoncorretto:17-alpine
WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY --from=builder /build/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
