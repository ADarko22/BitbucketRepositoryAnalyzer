plugins {
    kotlin("jvm") version "2.1.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.json:json:20250107")

    implementation("org.eclipse.jgit:org.eclipse.jgit:7.2.0.202503040940-r")
    implementation("commons-io:commons-io:2.18.0")

    implementation("ch.qos.logback:logback-classic:1.2.11")

    implementation("org.apache.maven:maven-embedder:3.8.1")
    implementation("org.apache.maven:maven-compat:3.8.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
