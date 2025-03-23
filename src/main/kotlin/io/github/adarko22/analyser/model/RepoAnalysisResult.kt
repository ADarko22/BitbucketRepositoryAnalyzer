package io.github.adarko22.analyser.model

data class RepoAnalysisResult(
    val repoName: String,
    val analysisInfo: List<String>
) {
    override fun toString(): String {
        val analysisDetails = analysisInfo.joinToString("\n\t- ")
        return """
            Repository: $repoName
            Analysis Information:
                $analysisDetails
        """.trimIndent()
    }
}
