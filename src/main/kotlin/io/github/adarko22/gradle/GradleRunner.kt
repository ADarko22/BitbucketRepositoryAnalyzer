package io.github.adarko22.gradle

import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.model.GradleProject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.nio.file.Path

class GradleRunner {

    private val logger: Logger = LoggerFactory.getLogger(GradleRunner::class.java)

    fun runGradleMavenDependencyTree(repoDir: Path): Map<String, List<String>> =
        GradleConnector.newConnector()
            .forProjectDirectory(repoDir.toFile())
            .connect()
            .use { connection ->
                val rootProject = connection.getModel(GradleProject::class.java)
                val allProjects = listOf(rootProject) + rootProject.children

                allProjects.associate { project ->
                    val projectPath = project.path.removePrefix(":")

                    logger.info("Running `gradle dependency` in {}", projectPath)
                    projectPath to runDependenciesTask(connection, projectPath)
                }
            }

    private fun runDependenciesTask(connection: ProjectConnection, projectPath: String): List<String> {
        val outputStream = ByteArrayOutputStream()

        try {
            connection.newBuild()
                .withArguments("--quiet")
                .setStandardOutput(outputStream)
                .forTasks("$projectPath:dependencies")
                .run()
        } catch (ex: Exception) {
            logger.error("Error running dependencies task for project $projectPath: $ex.message")
        }

        return outputStream.toString().lineSequence().filter(String::isNotEmpty).toList()
    }
}
