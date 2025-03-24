package io.github.adarko22

import io.github.adarko22.analyser.*
import io.github.adarko22.analyser.repo.FilterDependencyRegex
import io.github.adarko22.analyser.repo.FilterDependencyStrategy
import io.github.adarko22.analyser.repo.RepositoriesAnalyser
import io.github.adarko22.bitbucket.BitbucketApiClient
import io.github.adarko22.bitbucket.RepoCloner
import io.github.adarko22.maven.MavenRunner
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("Main")

    val (bitbucketUrl, username, password) = loadConfig()
    val projectKeys = loadProjectKeys()
    val projectAnalyzer = initializeProjectAnalyzer(bitbucketUrl, username, password)

    runBlocking {
        try {
            val result = projectAnalyzer.analyzeAllProjectsAndGenerateReport(projectKeys)

            logger.info("\n===== Summary of All Analysis Results=====")
            result.forEach { logger.info("\n$it") }
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
 * Loads the list of project keys from system environment variables or defaults to a fallback value.
 */
fun loadProjectKeys(): List<String> {
    val projectKeysEnv = System.getenv("PROJECT_KEYS") ?: ""
    return projectKeysEnv.split(",").map { it.trim() }
}

/**
 * Initializes and returns a [ProjectsAnalyzer] instance.
 */
fun initializeProjectAnalyzer(bitbucketUrl: String, username: String, password: String): ProjectsAnalyzer {
    val repoCloner = RepoCloner(username, password)
    val mavenRunner = MavenRunner()
    val filterDependencyRegex = loadFilterDependencyRegex()
    val strategy = FilterDependencyStrategy(filterDependencyRegex, mavenRunner)
    val reposAnalyser = RepositoriesAnalyser(strategy, repoCloner)
    val bitbucketApiClient = BitbucketApiClient(bitbucketUrl, username, password)

    return ProjectsAnalyzer(reposAnalyser, bitbucketApiClient)
}

/**
 * Loads the regular expression for filtering dependencies.
 * Defaults to a common Tomcat pattern if not found in the system environment.
 */
fun loadFilterDependencyRegex(): Regex =
    System.getenv("DEPENDENCY_REGEX")?.takeIf { it.isNotEmpty() }
        ?.let { Regex(it) } ?: FilterDependencyRegex.TOMCAT.regex

