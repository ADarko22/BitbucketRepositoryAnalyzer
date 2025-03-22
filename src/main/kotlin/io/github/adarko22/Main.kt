package io.github.adarko22

import io.github.adarko22.analyser.ProjectsAnalyzer
import io.github.adarko22.analyser.RepositoriesAnalyser
import io.github.adarko22.analyser.TomcatRepositoryAnalysisStrategy
import io.github.adarko22.bitbucket.BitbucketApiClient
import io.github.adarko22.bitbucket.RepoCloner
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

fun main() {
    val bitbucketUrl = "BITBUCKET URL"
    val username = "YOUR USERNAME"
    val password = "YOUR PASSWORD"

    val logger = LoggerFactory.getLogger("Main")

    // Setup services
    val repoCloner = RepoCloner(username, password)
    val reposAnalyser = RepositoriesAnalyser(TomcatRepositoryAnalysisStrategy(), repoCloner)
    val bitbucketApiClient = BitbucketApiClient(bitbucketUrl, username, password)
    val projectAnalyzer = ProjectsAnalyzer(reposAnalyser, bitbucketApiClient)

    // Run the analysis
    runBlocking {
        val result = projectAnalyzer.analyzeAllProjectsAndGenerateReport()

        logger.info("Summary of all info collected:")
        result.forEach { logger.info(it.toString()) }
    }

}

