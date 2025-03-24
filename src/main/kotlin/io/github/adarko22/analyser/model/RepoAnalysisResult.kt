package io.github.adarko22.analyser.model

data class RepoAnalysisResult(
    val repoName: String,
    val analysisInfo: List<String>
) {
    override fun toString(): String {
        val analysisInfoFormatted = analysisInfo.joinToString(separator = "\n") { "\t\t- $it" }
        return buildString {
            appendLine("Repository: $repoName")
            appendLine("\tAnalysis Information:")
            appendLine(analysisInfoFormatted)
        }
    }
}
