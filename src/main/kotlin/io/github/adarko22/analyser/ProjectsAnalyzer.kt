package io.github.adarko22.analyser

import io.github.adarko22.analyser.model.ProjectAnalysisResult
import io.github.adarko22.analyser.model.ProjectAnalysisResultList
import io.github.adarko22.analyser.model.toProjectAnalysisResultList
import io.github.adarko22.analyser.model.toRepoAnalysisResultList
import io.github.adarko22.analyser.repo.RepositoriesAnalyser
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

    suspend fun analyzeAllProjectsAndGenerateReport(targetProjectKeys: List<String> = emptyList()): ProjectAnalysisResultList {
        val projectKeys = targetProjectKeys.ifEmpty { bitbucketApiClient.getProjectKeys() }
        val chunkSize = projectKeys.size / numCores + 1

        // Split projectKeys across available CPU cores
        return coroutineScope {
            projectKeys.chunked(chunkSize).map { chunk ->
                async { analyseProjects(chunk) }
            }.awaitAll()
        }.flatten()
            .toProjectAnalysisResultList()
    }

    private fun analyseProjects(projectKeys: List<String>): List<ProjectAnalysisResult> {
        return projectKeys.map { projectKey ->
            logger.info("Analysing project $projectKey")
            val reposCloneLinks = bitbucketApiClient.getReposLinksForProjectKey(projectKey)
            val analysisResults = reposAnalyser.analyseRepos(reposCloneLinks)
            ProjectAnalysisResult(projectKey, analysisResults)
        }
    }
}
