plugins {
	application
	checkstyle
	jacoco
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "7.2.2.6593"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

application {
	mainClass = "hexlet.code.AppApplication"
}

repositories {
	mavenCentral()
}

val lombokVersion = "1.18.38"
val postgresqlVersion = "42.3.3"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation ("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation ("org.springframework.data:spring-data-jpa")
	implementation ("org.liquibase:liquibase-core")
	implementation ("org.postgresql:postgresql:$postgresqlVersion")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")

	implementation ("org.springframework.boot:spring-boot-starter-actuator")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation ("org.springframework.boot:spring-boot-devtools")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.15")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")

	implementation ("net.datafaker:datafaker:2.4.3")

	implementation("org.instancio:instancio-junit:3.3.0")
	implementation ("org.instancio:instancio-core:1.6.0")

	annotationProcessor ("org.projectlombok:lombok:$lombokVersion")
	annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	compileOnly ("org.projectlombok:lombok:$lombokVersion")
	runtimeOnly ("com.h2database:h2")

	testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:4.1.1")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
