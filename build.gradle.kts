plugins {
    kotlin("jvm") version "2.1.10"
    java
    application
    jacoco
    id("org.sonarqube") version "6.0.1.5171"
}

group = "io.github.adarko22"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Rest API
    implementation(libs.httpclient)
    implementation(libs.okhttp)
    implementation(libs.json)

    // Git
    implementation(libs.jgit)

    // Maven
    implementation(libs.mavenInvoker)

    // Kotlin Coroutines
    implementation(libs.coroutines)

    // Logging
    implementation(libs.logback)

    // Utils
    implementation(libs.commonsIo)

    // Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockitoCore)
    testImplementation(libs.mockitoKotlin)
    testImplementation(libs.mockitoJUnit)
    testImplementation(libs.assertj)

    testImplementation(libs.mockwebserver)
    testImplementation(libs.okhttp)
    testImplementation(libs.json)
}


kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("io.github.adarko22.MainKt")
}

jacoco {
    toolVersion = "0.8.12"
}

sourceSets {
    test {
        resources {
            srcDir("src/test/resources")
        }
    }
}

sonar {
    properties {
        property("sonar.projectKey", "ADarko22_BitbucketRepositoryAnalyzer")
        property("sonar.organization", "adarko22-dev")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.processTestResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}