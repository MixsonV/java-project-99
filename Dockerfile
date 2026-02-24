FROM gradle:8.12.1-jdk21

RUN ["./gradlew", "clean", "installDist"]

CMD ["./build/install/app/bin/app"]