package io.github.adarko22.analyser.model

data class ProjectAnalysisResult(
    val projectKey: String,
    val repoAnalysisResultList: List<RepoAnalysisResult>
) {
    override fun toString(): String {
        val repoAnalysisFormatted = repoAnalysisResultList.joinToString(separator = "\n") { "- $it" }
        return buildString {
            appendLine("Project: $projectKey")
            appendLine("Repositories Analysis:")
            appendLine(repoAnalysisFormatted)
        }
    }
}

