plugins {
	application
	checkstyle
	jacoco
	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "7.2.2.6593"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

application {
	mainClass = "hexlet.code.AppApplication"
}

repositories {
	mavenCentral()
}

val lombokVersion = "1.18.38";

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	compileOnly("org.projectlombok:lombok:$lombokVersion")
	annotationProcessor("org.projectlombok:lombok:$lombokVersion")
	testCompileOnly("org.projectlombok:lombok:$lombokVersion")
	testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
	implementation("org.postgresql:postgresql:42.3.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

checkstyle {
	toolVersion = "10.12.0"
	configDirectory.set(file("config/checkstyle"))
}

tasks.withType<Checkstyle>().configureEach {
	classpath = files("${project.rootDir}/src/test/java")
}

val myCheckstyleTest by tasks.registering(Checkstyle::class) {
	source("src/test/java")
	classpath = files()
	configFile = file("${project.rootDir}/config/checkstyle/checkstyle.xml")
	include("**/*.java")
	exclude("**/generated/**")
}

tasks.named("check") {
	dependsOn(myCheckstyleTest)
}

jacoco {
	toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		html.required.set(true)
		csv.required.set(false)
	}
}

tasks.withType<Test> {
	finalizedBy(tasks.jacocoTestReport)
}

sonar {
	properties {
		property("sonar.projectKey", "MixsonV_java-project-99")
		property("sonar.organization", "mixsonv")
		property("sonar.host.url", "https://sonarcloud.io")
		property ("sonar.login", System.getenv("SONAR_TOKEN") ?: "")
		property("sonar.java.binaries", "build/classes/java/main")
		property ("sonar.java.coveragePlugin", "jacoco")
		property ("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
	}
}
