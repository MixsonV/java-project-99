clean:
	./gradlew clean

build:
	./gradlew installDist

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

sonar:
	./gradlew sonar

lint:
	./gradlew checkstyleMain

run:
	./gradlew bootRun

.PHONY: build