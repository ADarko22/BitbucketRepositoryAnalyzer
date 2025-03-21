package org.example.org.example

import org.apache.commons.io.FileUtils
import org.example.RepositoryAnalysisStrategy
import java.io.File

class RepositoriesAnalyzer(bitbucketUrl: String, username: String, password: String) {
    private val apiClient = BitbucketApiClient(bitbucketUrl, username, password)
    private val cloner = RepoCloner(username, password)

    fun analzyeAllRepos(strategy: RepositoryAnalysisStrategy): Map<String, String> {
        val allReposAnalysisResult = mutableMapOf<String, String>()
        val projectKeys = apiClient.getProjectKeys()
        println("Found ${projectKeys.size} projects")

        for (projectKey in projectKeys) {
            val reposCloneLinks = apiClient.getReposCloneLinksBy(projectKey)
            println("Found ${reposCloneLinks.size} repositories for project $projectKey")

            for (repoIdx in reposCloneLinks.indices) {
                val repoCloneLink = reposCloneLinks[repoIdx]
                val repoName = repoCloneLink.substring(repoCloneLink.lastIndexOf('/') + 1, repoCloneLink.lastIndexOf("."))
                val repoDir = File("${System.getProperty("user.dir")}/$repoName").toPath()
                println("Repo #${repoIdx + 1}: $repoName")
                try {
                    FileUtils.deleteDirectory(repoDir.toFile())
                    cloner.cloneRepo(repoCloneLink, repoDir)
                    allReposAnalysisResult[repoName] = strategy.analyzeRepo(repoDir).joinToString(separator = "\n")
                } catch (e: Exception) {
                    println("Error processing repository $repoName: ${e.message}")
                } finally {
                    FileUtils.deleteDirectory(repoDir.toFile())
                }
            }
        }

        return allReposAnalysisResult
    }
}