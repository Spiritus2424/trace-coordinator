/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.5.1/userguide/building_java_projects.html
 */

group = "org.eclipse"
version = "v1.0.0-alpha"

plugins {
    java
	application
    `maven-publish`
}
application {
    mainClass.set("org.eclipse.trace.coordinator.Main")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
    mavenLocal()

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/spiritus2424/tsp-java-client")
        credentials {
            username = System.getenv("GITHUB_USER")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}


dependencies {

    // This dependency is used by the application.
    implementation("jakarta.platform:jakarta.jakartaee-api:10.0.0")
    implementation("org.glassfish.jersey.core:jersey-server:3.1.1")
	
	implementation("org.glassfish.jersey.containers:jersey-container-grizzly2-http:3.1.1")

	implementation("org.glassfish.jersey.inject:jersey-hk2:3.1.1")
	annotationProcessor("org.glassfish.hk2:hk2-metadata-generator:3.0.4")
	
	// For WALD
	implementation("org.glassfish.jaxb:jaxb-runtime:4.0.2")
	
	// This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:31.0.1-jre")

    // Tsp Java Client
    implementation("org.eclipse:tsp-java-client-insiders:v2.6.17-alpha")

    // Jackson
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")

    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

	// Lombok - Decorator
    compileOnly("org.projectlombok:lombok:1.18.26")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
	testCompileOnly("org.projectlombok:lombok:1.18.26")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.26")


}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}


tasks.jar {
	archiveFileName.set("${project.name}.jar")
}
