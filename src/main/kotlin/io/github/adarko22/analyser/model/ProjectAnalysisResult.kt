package io.github.adarko22.analyser.model

import org.json.JSONObject

data class ProjectAnalysisResult(
    val projectKey: String,
    val repoAnalysisResultList: RepoAnalysisResultList
) {
    override fun toString(): String {
        return buildString {
            appendLine("Project: $projectKey")
            appendLine("Repositories Analysis:")
            appendLine(repoAnalysisResultList)
        }
    }

    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("projectKey", projectKey)
        json.put("repoAnalysisResultList", repoAnalysisResultList.toJson())
        return json
    }
}


