FROM maven:3.9.9-amazoncorretto-23
WORKDIR /src
COPY . .
ENV SPRING_PROFILES_ACTIVE=prod
RUN mvn clean package
CMD mvn spring-boot:run
