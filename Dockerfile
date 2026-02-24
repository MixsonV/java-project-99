FROM gradle:8.12.1-jdk21

COPY . /app
WORKDIR /app

RUN chmod +x ./gradlew

RUN ["./gradlew", "clean", "installDist"]

CMD ["./build/install/java-project-99/bin/java-project-99"]