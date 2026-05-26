#FROM eclipse-temurin
#
#WORKDIR /app
#
#COPY build/libs/tracker-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "app.jar"]


FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean build -x test

EXPOSE 8080

CMD ["java", "-jar", "build/libs/tracker-0.0.1-SNAPSHOT.jar"]