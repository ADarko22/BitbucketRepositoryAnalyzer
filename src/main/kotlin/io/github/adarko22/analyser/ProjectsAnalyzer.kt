package io.github.adarko22.analyser

import io.github.adarko22.analyser.model.ProjectAnalysisResult
import io.github.adarko22.bitbucket.BitbucketApiClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory

class ProjectsAnalyzer(
    private val reposAnalyser: RepositoriesAnalyser,
    private val bitbucketApiClient: BitbucketApiClient
) {
    private val logger = LoggerFactory.getLogger(ProjectsAnalyzer::class.java)
    private val numCores = Runtime.getRuntime().availableProcessors()

    suspend fun analyzeAllProjectsAndGenerateReport(): List<ProjectAnalysisResult> {
        val projectKeys = bitbucketApiClient.getProjectKeys()
        val chunkSize = projectKeys.size / numCores + 1

        // Split projectKeys across available CPU cores
        return coroutineScope {
            projectKeys.chunked(chunkSize).map { chunk ->
                async { analyzeProjects(chunk) }
            }.awaitAll()
        }.flatten()
    }

    private fun analyzeProjects(projectKeys: List<String>): List<ProjectAnalysisResult> {
        return projectKeys.map { projectKey ->
            logger.info("Analysing project $projectKey")
            val reposCloneLinks = bitbucketApiClient.getReposLinksForProjectKey(projectKey)
            val analysisResults = reposAnalyser.analyseRepos(reposCloneLinks)
            ProjectAnalysisResult(projectKey, analysisResults)
        }
    }
}
