package io.github.adarko22.analyser.repo

import io.github.adarko22.analyser.model.RepoAnalysisResult
import io.github.adarko22.gradle.GradleRunner
import io.github.adarko22.maven.MavenRunner
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name

class FilterDependencyStrategy(
    private val filterDependencyRegex: Regex,
    private val mavenRunner: MavenRunner = MavenRunner(),
    private val gradleRunner: GradleRunner = GradleRunner()
) : RepositoryAnalysisStrategy {

    override fun analyseRepo(repoDir: Path): RepoAnalysisResult {
        val result = when {
            isMavenProject(repoDir) -> analyseMavenRepo(repoDir)
            isGradleProject(repoDir) -> analyseGradleRepo(repoDir)
            else -> listOf("Not a Supported Project!")
        }
        return RepoAnalysisResult(repoDir.fileName.name, result)
    }

    private fun analyseMavenRepo(repoDir: Path): List<String> =
        mavenRunner.runMavenDependencyTree(repoDir).processDependencies()

    private fun analyseGradleRepo(repoDir: Path): List<String> =
        gradleRunner.runGradleMavenDependencyTree(repoDir).flatMap { it.value.processDependencies() }.distinct()

    private fun List<String>.processDependencies(): List<String> =
        this.mapNotNull { line -> filterDependencyRegex.find(line)?.value }
            .distinct()

    private fun isMavenProject(repoDir: Path) = Files.exists(repoDir.resolve("pom.xml"))

    private fun isGradleProject(repoDir: Path) =
        listOf("build.gradle", "build.gradle.kts").any { Files.exists(repoDir.resolve(it)) }
}
