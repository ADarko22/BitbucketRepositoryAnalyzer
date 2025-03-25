package io.github.adarko22

import io.github.adarko22.analyser.ProjectsAnalyzer
import io.github.adarko22.analyser.repo.FilterDependencyStrategy
import io.github.adarko22.analyser.repo.RepositoriesAnalyser
import io.github.adarko22.bitbucket.BitbucketApiClient
import io.github.adarko22.bitbucket.RepoCloner
import io.github.adarko22.maven.MavenRunner
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.io.File

fun main() {
    val logger = LoggerFactory.getLogger("Main")
    val config = Config.load()
    val projectsAnalyzer = initializeProjectAnalyzer(config)

    runBlocking {
        try {
            val result = projectsAnalyzer.analyzeAllProjectsAndGenerateReport(config.projectKeys)
            logger.info("\n\n===== Summary of All Analysis Results=====\n$result")
            // Write to file
            val outputFile = File("analysis_report.json")
            outputFile.writeText(result.toJson().toString(4)) // Pretty print with 4 spaces indentation

        } catch (e: Exception) {
            logger.error("Error analyzing projects: ${e.message}", e)
        }
    }
}

/**
 * Initializes and returns a [ProjectsAnalyzer] instance.
 */
private fun initializeProjectAnalyzer(config: Config): ProjectsAnalyzer {
    val repoCloner = RepoCloner(config.username, config.password)
    val mavenRunner = MavenRunner()
    val strategy = FilterDependencyStrategy(config.dependencyRegex, mavenRunner)
    val reposAnalyser = RepositoriesAnalyser(strategy, repoCloner)
    val bitbucketApiClient = BitbucketApiClient(config.bitbucketUrl, config.username, config.password)

    return ProjectsAnalyzer(reposAnalyser, bitbucketApiClient)
}
