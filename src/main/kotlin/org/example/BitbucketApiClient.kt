package org.example.org.example

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class BitbucketApiClient(private val baseUrl: String, private val username: String, private val password: String) {

    private val client = OkHttpClient()

    /**
     * Refer to: https://docs.atlassian.com/bitbucket-server/rest/5.16.0/bitbucket-rest.html#idm8297225296
     */
    fun getProjectKeys() = getAllValues("/rest/api/1.0/projects")
        .map { it.getString("key") }

    /**
     * Refer to: https://docs.atlassian.com/bitbucket-server/rest/5.16.0/bitbucket-rest.html#idm8287387248
     */
    fun getReposCloneLinksBy(projectKey: String) = getAllValues("/rest/api/1.0/projects/$projectKey/repos")
        .map { it.getJSONObject("links") }
        .map {
            val clones = it.getJSONArray("clone")
            for (i in 0 until clones.length())
                if (clones.getJSONObject(i).getString("name") == "http")
                    return@map clones.getJSONObject(i).getString("href")
            return@map null
        }
        .filterNotNull()

    /**
     * Refer to Paged APIs: https://docs.atlassian.com/bitbucket-server/rest/5.16.0/bitbucket-rest.html
     *     {
     *         "size": 3,
     *         "limit": 3,
     *         "isLastPage": false,
     *         "values": [
     *             { /* result 0 */ },
     *             { /* result 1 */ },
     *             { /* result 2 */ }
     *         ],
     *         "start": 0,
     *         "filter": null,
     *         "nextPageStart": 3
     *     }
     */
    private fun getAllValues(endpoint: String): List<JSONObject> {
        val result = mutableListOf<JSONObject>()
        var page = 0
        var isLastPage = false

        while (!isLastPage) {
            val request = Request.Builder()
                .url("$baseUrl$endpoint?start=$page")
                .header("Authorization", Credentials.basic(username, password))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val responseBody = response.body?.string() ?: throw IOException("Empty response body")
                val jsonResponse = JSONObject(responseBody)

                val items = jsonResponse.getJSONArray("values")
                for (i in 0 until items.length())
                    result.add(items.getJSONObject(i))

                // Check if there's a next page
                isLastPage = jsonResponse.getBoolean("isLastPage")
                page = if (!isLastPage) jsonResponse.getInt("nextPageStart") else page
            }
        }

        return result
    }
}