package io.github.adarko22.maven

import io.github.adarko22.Config
import org.apache.maven.shared.invoker.DefaultInvocationRequest
import org.apache.maven.shared.invoker.DefaultInvoker
import org.apache.maven.shared.invoker.InvocationResult
import org.apache.maven.shared.invoker.Invoker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

class MavenRunner(
    private val invoker: Invoker = DefaultInvoker(),
    private val logger: Logger = LoggerFactory.getLogger(MavenRunner::class.java)
) {

    init {
        val mavenHome = Config.detectMavenHome()
        require(mavenHome.toFile().exists()) { "Maven home does not exist: $mavenHome" }
        invoker.mavenHome = mavenHome.toFile()
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
