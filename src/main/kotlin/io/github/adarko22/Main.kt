package io.github.adarko22

import io.github.adarko22.analyser.ProjectsAnalyzer
import io.github.adarko22.analyser.RepositoriesAnalyser
import io.github.adarko22.analyser.TomcatRepositoryAnalysisStrategy
import io.github.adarko22.bitbucket.BitbucketApiClient
import io.github.adarko22.bitbucket.RepoCloner
import io.github.adarko22.maven.MavenRunner
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("Main")

    val (bitbucketUrl, username, password) = loadConfig()
    val projectAnalyzer = initializeProjectAnalyzer(bitbucketUrl, username, password)

    runBlocking {
        try {
            val result = projectAnalyzer.analyzeAllProjectsAndGenerateReport()

            logger.info("===== Summary of All Analysis Results=====")
            result.forEach { logger.info("- $it") }
        } catch (e: Exception) {
            logger.error("Error analyzing projects: ${e.message}", e)
        }
    }
}

/**
 * Loads required configuration values, failing if any are missing.
 */
fun loadConfig(): Triple<String, String, String> {
    val bitbucketUrl = System.getenv("BITBUCKET_URL") ?: error("Missing environment variable: BITBUCKET_URL")
    val username = System.getenv("BITBUCKET_USERNAME") ?: error("Missing environment variable: BITBUCKET_USERNAME")
    val password = System.getenv("BITBUCKET_PASSWORD") ?: error("Missing environment variable: BITBUCKET_PASSWORD")

    return Triple(bitbucketUrl, username, password)
}

/**
 * Initializes and returns a [ProjectsAnalyzer] instance.
 */
fun initializeProjectAnalyzer(bitbucketUrl: String, username: String, password: String): ProjectsAnalyzer {
    val repoCloner = RepoCloner(username, password)
    val mavenRunner = MavenRunner()
    val strategy = TomcatRepositoryAnalysisStrategy(mavenRunner)
    val reposAnalyser = RepositoriesAnalyser(strategy, repoCloner)
    val bitbucketApiClient = BitbucketApiClient(bitbucketUrl, username, password)

    return ProjectsAnalyzer(reposAnalyser, bitbucketApiClient)
}
