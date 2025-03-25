package io.github.adarko22.bitbucket

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BitbucketApiClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var bitbucketApiClient: BitbucketApiClient

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val baseUrl = mockWebServer.url("/baseUrl").toString()
        bitbucketApiClient = BitbucketApiClient(baseUrl, "username", "password")
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getProjectKeys should return list of project keys`() {
        val mockResponse = """
            {
                "size": 1,
                "limit": 25,
                "isLastPage": true,
                "values": [
                    {"key": "PROJ1"}
                ],
                "start": 0
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockResponse))

        val projectKeys = bitbucketApiClient.getProjectKeys()

        verifyRequestPath("/baseUrl/rest/api/1.0/projects?start=0")
        verifyResponse(projectKeys, listOf("PROJ1"))
    }

    @Test
    fun `getReposLinksForProjectKey should return list of repository clone URLs`() {
        val mockResponse = """
            {
                "size": 1,
                "limit": 25,
                "isLastPage": true,
                "values": [
                    {
                        "links": {
                            "clone": [
                                {"href": "http://example.com/scm/proj1/repo1.git", "name": "http"}
                            ]
                        }
                    }
                ],
                "start": 0
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockResponse))

        val repoLinks = bitbucketApiClient.getReposLinksForProjectKey("PROJ1")

        verifyRequestPath("/baseUrl/rest/api/1.0/projects/PROJ1/repos?start=0")
        verifyResponse(repoLinks, listOf("http://example.com/scm/proj1/repo1.git"))
    }

    private fun verifyRequestPath(expectedPath: String) {
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals(expectedPath, recordedRequest.path)
    }

    private fun <T> verifyResponse(actual: List<T>, expected: List<T>) {
        assertNotNull(actual)
        assertEquals(expected.size, actual.size)
        assertEquals(expected, actual)
    }
}