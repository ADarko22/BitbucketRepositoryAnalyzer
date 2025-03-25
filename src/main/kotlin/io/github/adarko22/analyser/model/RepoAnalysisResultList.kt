package io.github.adarko22.analyser.model

import org.json.JSONArray
import org.json.JSONObject

class RepoAnalysisResultList(val repoAnalysisResults: List<RepoAnalysisResult>) {
    override fun toString() = repoAnalysisResults.joinToString(separator = "\n")

    fun toJson(): JSONObject {
        val jsonArray = JSONArray()

        for (repoAnalysisResult in repoAnalysisResults) {
            jsonArray.put(repoAnalysisResult.toJson())
        }

        return JSONObject().put("repoAnalysisResultList", jsonArray)
    }
}

fun List<RepoAnalysisResult>.toRepoAnalysisResultList() = RepoAnalysisResultList(this)