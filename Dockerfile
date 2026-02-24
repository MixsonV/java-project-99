FROM gradle:8.12.1-jdk21

WORKDIR /java-project-99

COPY . .

RUN chmod +x ./gradlew

RUN ["./gradlew", "clean", "installDist"]

CMD ["./build/install/app/bin/app"]