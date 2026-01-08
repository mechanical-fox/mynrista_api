
FROM maven:4.0.0-rc-4-eclipse-temurin-25-noble

ADD  .  /app/
WORKDIR /app
EXPOSE 8080

RUN mvn package -DskipTests

CMD ["java", "-jar", "target/app.jar", "--spring.profiles.active=prod","--spring.datasource.password=${DATABASE_PASSWORD}"]
