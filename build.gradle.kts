plugins {
    kotlin("jvm") version "2.1.10"
    application
}

group = "io.github.adarko22"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Rest API
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.json:json:20250107")
    // Git
    implementation("org.eclipse.jgit:org.eclipse.jgit:7.2.0.202503040940-r")
    // Maven
    implementation("org.apache.maven.plugins:maven-invoker-plugin:3.8.1")
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    // Logging
    implementation("ch.qos.logback:logback-classic:1.2.11")
    // Utils
    implementation("commons-io:commons-io:2.18.0")

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.slf4j:slf4j-api:2.0.9")
    testImplementation("org.slf4j:slf4j-simple:2.0.9")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("io.github.adarko22.MainKt")
}