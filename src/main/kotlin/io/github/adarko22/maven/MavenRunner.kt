package io.github.adarko22.maven

import org.apache.maven.shared.invoker.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

private const val MAVEN_HOME_NOT_DETECTED =
    "Maven home could not be detected. Please set the M2_HOME or MAVEN_HOME environment variable."

class MavenRunner(
    private val mavenHome: Path = detectMavenHome(),
    private val invoker: Invoker = DefaultInvoker(),
    private val logger: Logger = LoggerFactory.getLogger(MavenRunner::class.java)
) {

    init {
        require(mavenHome.toFile().exists()) { "Maven home does not exist: $mavenHome" }
        invoker.mavenHome = mavenHome.toFile()
    }

    companion object {
        private fun detectMavenHome(): Path {
            return System.getenv("M2_HOME")?.let { Path.of(it) }
                ?: System.getenv("MAVEN_HOME")?.let { Path.of(it) }
                ?: Path.of(System.getProperty("user.home"), ".sdkman/candidates/maven/current")
                    .takeIf { it.toFile().exists() }
                ?: throw IllegalStateException(MAVEN_HOME_NOT_DETECTED)
        }
    }

    fun runMavenDependencyTree(repoDir: Path): List<String> {
        val outputLines = mutableListOf<String>()

        val request = DefaultInvocationRequest()
            .setPomFile(repoDir.resolve("pom.xml").toFile())
            .addArg("dependency:tree")
            .setOutputHandler { outputLines.add(it) }
            .setErrorHandler { logger.error(it) }
            .setBatchMode(true)

        logger.info("Running `mvn dependency:tree` in {}", repoDir.toAbsolutePath())

        val result: InvocationResult = invoker.execute(request)

        if (result.exitCode != 0) {
            logger.error("Maven execution failed with exit code: ${result.exitCode}")
            result.executionException?.let { logger.error("Error details:", it) }
        }

        return outputLines
    }
}