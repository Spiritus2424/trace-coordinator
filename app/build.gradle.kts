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
    war
    `maven-publish`
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
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

    // This dependency is used by the application.
    providedCompile("jakarta.platform:jakarta.jakartaee-api:10.0.0")
    
    implementation("org.eclipse:tsp-java-client:v1.1.1-alpha")
    
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.1")

}


tasks.war {
    archiveBaseName.set("trace-coordinator")
}


tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
