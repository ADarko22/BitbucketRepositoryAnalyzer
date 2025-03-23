package io.github.adarko22.analyser.model

data class ProjectAnalysisResult(
    val projectKey: String,
    val repoAnalysisResultList: List<RepoAnalysisResult>
) {
    override fun toString(): String {
        val repoResults = repoAnalysisResultList.joinToString("\n\t- ")
        return """
            Project: $projectKey
            Repositories Analysis:
                $repoResults
        """.trimIndent()
    }
}

