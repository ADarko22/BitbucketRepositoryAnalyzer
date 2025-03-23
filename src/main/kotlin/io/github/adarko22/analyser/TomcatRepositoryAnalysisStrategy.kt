package io.github.adarko22.analyser

import io.github.adarko22.analyser.model.RepoAnalysisResult
import io.github.adarko22.maven.MavenRunner
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.pathString

class TomcatRepositoryAnalysisStrategy(
    private val mavenRunner: MavenRunner = MavenRunner()
) : RepositoryAnalysisStrategy {

    private val logger = LoggerFactory.getLogger(TomcatRepositoryAnalysisStrategy::class.java)
    private val tomcatPattern = Regex("org.apache.tomcat[^:]*:\\S+:(\\d+\\.\\d+\\.\\d+)(?:\\.\\d+)?")

    override fun analyseRepo(repoDir: Path): RepoAnalysisResult {
        val result = when {
            isMavenProject(repoDir) -> {
                logger.info("Analyzing Dependency Tree for Tomcat dependencies:")
                mavenRunner.runMavenDependencyTree(repoDir)
                    .mapNotNull { line -> tomcatPattern.find(line)?.value }
                    .distinct()
                    .ifEmpty { listOf("No result found") }
            }

            else -> listOf("Not a Maven Project")
        }

        logger.info(result.joinToString("\n"))
        return RepoAnalysisResult(repoDir.fileName.name, result)
    }

    private fun isMavenProject(repoDir: Path) = Files.walk(repoDir, 1).anyMatch { it.fileName.pathString == "pom.xml" }
}
