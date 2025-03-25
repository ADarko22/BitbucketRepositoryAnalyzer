package io.github.adarko22.analyser.model

import org.json.JSONArray
import org.json.JSONObject

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

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("repoName", repoName)
        json.put("analysisInfo", JSONArray(analysisInfo))
        return json
    }
}
