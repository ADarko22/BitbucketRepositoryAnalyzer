package io.github.adarko22.bitbucket

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
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
        val baseUrl = mockWebServer.url("/").toString().removeSuffix("/")
        bitbucketApiClient = BitbucketApiClient(baseUrl, "username", "password")
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should detect project keys in Maven project`() {
        val mockResponse = """
            {
                "size": 1,
                "limit": 25,
                "isLastPage": true,
                "values": [{"key": "PROJ1"}],
                "start": 0
            }
        """.trimIndent()

        testApiCall(
            "/rest/api/1.0/projects?start=0",
            mockResponse,
            { bitbucketApiClient.getProjectKeys() },
            listOf("PROJ1")
        )
    }

    @Test
    fun `should detect repository clone URLs for project key`() {
        val mockResponse = """
            {
                "size": 1,
                "limit": 25,
                "isLastPage": true,
                "values": [{
                    "links": {
                        "clone": [{"href": "http://example.com/scm/proj1/repo1.git", "name": "http"}]
                    }
                }],
                "start": 0
            }
        """.trimIndent()

        testApiCall(
            "/rest/api/1.0/projects/PROJ1/repos?start=0",
            mockResponse,
            { bitbucketApiClient.getReposLinksForProjectKey("PROJ1") },
            listOf("http://example.com/scm/proj1/repo1.git")
        )
    }

    @Test
    fun `should return null when 'clone' array is empty`() {
        val mockResponse = """
            {
                "size": 1,
                "limit": 25,
                "isLastPage": true,
                "values": [{
                    "links": {
                        "clone": []
                    }
                }],
                "start": 0
            }
        """.trimIndent()

        testApiCall(
            "/rest/api/1.0/projects/PROJ1/repos?start=0",
            mockResponse,
            { bitbucketApiClient.getReposLinksForProjectKey("PROJ1") },
            emptyList()
        )
    }

    private fun testApiCall(
        expectedPath: String,
        mockResponse: String,
        apiCall: () -> List<String>,
        expectedResponse: List<String>
    ) {
        mockWebServer.enqueue(MockResponse().setBody(mockResponse))

        val response = apiCall()

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals(expectedPath, recordedRequest.path)

        assertEquals(expectedResponse, response)
    }
}
