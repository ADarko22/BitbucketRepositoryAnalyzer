package io.github.adarko22.analyser

import io.github.adarko22.RepoAnalysisResult
import io.github.adarko22.bitbucket.RepoCloner
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import java.nio.file.Path

class RepositoriesAnalyser(
    private val strategy: RepositoryAnalysisStrategy,
    private val cloner: RepoCloner
) {
    private val logger = LoggerFactory.getLogger(RepositoriesAnalyser::class.java)

    fun analyseRepos(reposCloneLinks: List<String>): List<RepoAnalysisResult> =
        reposCloneLinks.mapNotNull { analyseRepo(it) }

    private fun analyseRepo(repoCloneLink: String): RepoAnalysisResult? {
        val repoName = extractRepoNameFrom(repoCloneLink)
        val repoDir = repoDirPath(repoName)

        return try {
            cleanDirectory(repoDir)
            cloner.cloneRepo(repoCloneLink, repoDir)
            strategy.analyseRepo(repoDir)
        } catch (e: Exception) {
            logger.error("Error processing repository {}: {}", repoName, e.message, e)
            null
        } finally {
            cleanDirectory(repoDir)
        }
    }

    private fun cleanDirectory(repoDir: Path) {
        try {
            FileUtils.deleteDirectory(repoDir.toFile())
        } catch (e: Exception) {
            logger.warn("Failed to delete directory {}: {}", repoDir, e.message)
        }
    }

    private fun extractRepoNameFrom(repoLink: String): String =
        repoLink.substringAfterLast('/').substringBeforeLast('.')

    private fun repoDirPath(repoName: String): Path = Path.of(System.getProperty("user.dir"), repoName)
}