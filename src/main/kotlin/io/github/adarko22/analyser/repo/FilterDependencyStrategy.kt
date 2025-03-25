package io.github.adarko22.analyser.repo

import io.github.adarko22.analyser.model.RepoAnalysisResult
import io.github.adarko22.maven.MavenRunner
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.pathString

class FilterDependencyStrategy(
    private val filterDependencyRegex: Regex,
    private val mavenRunner: MavenRunner = MavenRunner()
) : RepositoryAnalysisStrategy {

    private val logger = LoggerFactory.getLogger(FilterDependencyStrategy::class.java)

    override fun analyseRepo(repoDir: Path): RepoAnalysisResult {
        val result = when {
            isMavenProject(repoDir) -> {
                logger.info("Analyzing Dependency Tree for Tomcat dependencies:")
                mavenRunner.runMavenDependencyTree(repoDir)
                    .mapNotNull { line -> filterDependencyRegex.find(line)?.value }
                    .distinct()
                    .ifEmpty { listOf("No result found") }
            }

            else -> listOf("Not a Maven Project")
        }
        return RepoAnalysisResult(repoDir.fileName.name, result)
    }

    private fun isMavenProject(repoDir: Path) = Files.walk(repoDir, 1).anyMatch { it.fileName.pathString == "pom.xml" }
}
