package io.github.adarko22

data class RepoAnalysisResult(
    val repoName: String,
    val analysisInfo: List<String>
) {
    override fun toString(): String {
        val analysisDetails = analysisInfo.joinToString("\n  - ") { it }
        return """
            Repository: $repoName
            Analysis Information:
              - $analysisDetails
        """.trimIndent()
    }
}

data class ProjectAnalysisResult(
    val projectKey: String,
    val repoAnalysisResultList: List<RepoAnalysisResult>
) {
    override fun toString(): String {
        val repoResults = repoAnalysisResultList.joinToString("\n") { it.toString() }
        return """
            Project: $projectKey
            Repositories Analysis:
            $repoResults
        """.trimIndent()
    }
}
