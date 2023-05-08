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
            username = System.getenv("USERNAME")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}


dependencies {

    // This dependency is used by the application.
    implementation("jakarta.platform:jakarta.jakartaee-api:10.0.0")
    implementation("org.glassfish.jersey.core:jersey-server:3.1.1")
	implementation("org.glassfish.jersey.containers:jersey-container-jetty-http:3.1.1")
	
	implementation("org.glassfish.jersey.inject:jersey-hk2:3.1.1")
	annotationProcessor("org.glassfish.hk2:hk2-metadata-generator:3.0.4")
	
	// For WALD
	implementation("org.glassfish.jaxb:jaxb-runtime:4.0.2")

    // Tsp Java Client
    implementation("org.eclipse:tsp-java-client-insiders:v2.6.0-alpha")

    // Jackson
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")

    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
