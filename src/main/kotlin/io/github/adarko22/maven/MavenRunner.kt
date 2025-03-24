package io.github.adarko22.maven

import org.apache.maven.shared.invoker.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

private const val MAVEN_HOME_NOT_DETECTED =
    "Maven home could not be detected. Please set the M2_HOME or MAVEN_HOME environment variable."
private const val JAVA_HOME_NOT_DETECTED = "Java home could not be detected. Please set the JAVA_HOME environment variable."

class MavenRunner(
    private val mavenHome: Path = detectMavenHome(),
    private val invoker: Invoker = DefaultInvoker(),
    private val logger: Logger = LoggerFactory.getLogger(MavenRunner::class.java)
) {

    init {
        require(mavenHome.toFile().exists()) { "Maven home does not exist: $mavenHome" }
        invoker.mavenHome = mavenHome.toFile()
        configureJavaHome()
    }

    companion object {
        private fun detectMavenHome(): Path {
            return System.getProperty("M2_HOME")?.let { Path.of(it) }
                ?: System.getProperty("MAVEN_HOME")?.let { Path.of(it) }
                ?: Path.of(System.getProperty("user.home"), ".sdkman/candidates/maven/current")
                    .takeIf { it.toFile().exists() }
                ?: throw IllegalStateException(MAVEN_HOME_NOT_DETECTED)
        }

        private fun configureJavaHome() {
            if (System.getProperty("JAVA_HOME") == null) {
                val sdkmanJavaHome = Path.of(System.getProperty("user.home"), ".sdkman/candidates/java/current")
                if (sdkmanJavaHome.toFile().exists()) {
                    System.setProperty("JAVA_HOME", sdkmanJavaHome.toString())
                } else {
                    throw IllegalStateException(JAVA_HOME_NOT_DETECTED)
                }
            }
        }
    }

    fun runMavenDependencyTree(repoDir: Path): List<String> {
        val outputLines = mutableListOf<String>()

        val request = DefaultInvocationRequest()
            .setPomFile(repoDir.resolve("pom.xml").toFile())
            .addArg("-X dependency:tree")
            .setOutputHandler { outputLines.add(it) }
            .setErrorHandler { logger.error(it) }
            .setBatchMode(true)

        logger.debug("JAVA_HOME: ${System.getProperty("JAVA_HOME")}")
        logger.debug("invoker.mavenHome: {}", invoker.mavenHome)
        logger.info("Running `mvn dependency:tree` in {}", repoDir.toAbsolutePath())

        val result: InvocationResult = invoker.execute(request)

        if (result.exitCode != 0) {
            logger.error("Maven execution failed with exit code: ${result.exitCode}")
            result.executionException?.let { logger.error("Error details:", it) }
        }

        return outputLines
    }
}