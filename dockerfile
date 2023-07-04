FROM bellsoft/liberica-openjdk-alpine:17.0.3.1-2

WORKDIR /app

COPY . .

RUN apk add --no-cache maven

RUN mvn clean install

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]
