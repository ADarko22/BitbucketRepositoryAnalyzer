package org.example.org.example

fun main() {
    val bitbucketUrl = "BITBUCKET URL"
    val username = "YOUR USERNAME"
    val password = "YOUR PASSWORD"

    val repoInfoCollector = RepositoriesAnalyzer(bitbucketUrl, username, password)
    val repoInfo = repoInfoCollector.analzyeAllRepos(TomcatRepositoryAnalysisStrategy())

    println("\n\nSummary of all info collected:")
    println(repoInfo)
}