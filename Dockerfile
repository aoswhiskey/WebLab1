FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY libs ./libs
RUN mvn install:install-file -Dfile="libs/fastcgi-lib.jar" -DgroupId="itmo.web" -DartifactId="fastcgi" -Dversion="1.0" -Dpackaging="jar"
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

EXPOSE 1337
RUN mkdir /app
WORKDIR /app
COPY --from=build /app/target/app-jar-with-dependencies.jar app.jar

ENTRYPOINT ["java", "-DFCGI_PORT=1337", "-jar", "app.jar"]