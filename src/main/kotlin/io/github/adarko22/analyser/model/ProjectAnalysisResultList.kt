package io.github.adarko22.analyser.model

import org.json.JSONArray
import org.json.JSONObject

data class ProjectAnalysisResultList(val projectAnalysisResults: List<ProjectAnalysisResult>) {

    override fun toString() = projectAnalysisResults.joinToString(separator = "\n")

    fun toJson(): JSONObject {
        val jsonArray = JSONArray()

        for (projectAnalysisResult in projectAnalysisResults) {
            jsonArray.put(projectAnalysisResult.toJson())
        }

        return JSONObject().put("projectAnalysisResultList", jsonArray)
    }
}

fun List<ProjectAnalysisResult>.toProjectAnalysisResultList() = ProjectAnalysisResultList(this)