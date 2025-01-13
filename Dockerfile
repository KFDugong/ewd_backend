# Use the official Maven image with Java 21 as the base image
FROM jelastic/maven:3.9.5-openjdk-21

# Set the working directory inside the container
WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
