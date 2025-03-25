package io.github.adarko22

import io.github.adarko22.Config.Companion.USER_HOME
import io.github.adarko22.analyser.repo.FilterDependencyRegex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.any
import java.io.File
import java.nio.file.Path

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConfigTest {

    private val envVariables = mapOf(
        Config.BITBUCKET_URL to "https://bitbucket.example.com",
        Config.BITBUCKET_USERNAME to "username",
        Config.BITBUCKET_PASSWORD to "password",
        Config.PROJECT_KEYS to "project1, project2",
        Config.DEPENDENCY_REGEX to ".*junit.*"
    )

    @Test
    fun `should load configuration from environment variables`() {
        val config = Config.load(envVariables)
        assertEquals("https://bitbucket.example.com", config.bitbucketUrl)
        assertEquals("username", config.username)
        assertEquals("password", config.password)
        assertEquals(listOf("project1", "project2"), config.projectKeys)
        assertEquals(".*junit.*", config.dependencyRegex.pattern)
    }

    @Test
    fun `should apply default dependency regex when environment variable is missing`() {
        val envWithoutDependencyRegex = envVariables - Config.DEPENDENCY_REGEX
        val config = Config.load(envWithoutDependencyRegex)
        assertEquals(FilterDependencyRegex.TOMCAT.regex, config.dependencyRegex)
    }

    @Test
    fun `should throw error when required environment variables are missing`() {
        val envWithoutBitbucketUrl = envVariables - Config.BITBUCKET_URL
        assertThrows(IllegalStateException::class.java) {
            Config.load(envWithoutBitbucketUrl)
        }
    }

    @Test
    fun `detectMavenHome should resolve M2_HOME and MAVEN_HOME`() {
        val envWithM2Home = mapOf(Config.M2_HOME to "/maven/m2")
        assertEquals(Path.of("/maven/m2"), Config.detectMavenHome(envWithM2Home))

        val envWithMavenHome = mapOf(Config.MAVEN_HOME to "/maven/home")
        assertEquals(Path.of("/maven/home"), Config.detectMavenHome(envWithMavenHome))
    }

    @Test
    fun `detectMavenHome should fallback to SDKMAN path if it exists`() {
        val mockUserHomePath = mockPathFile(true)
        val pathMock = Mockito.mockStatic(Path::class.java)
        pathMock.`when`<Path> { Path.of(any(), any()) }
            .thenReturn(mockUserHomePath)
        assertEquals(mockUserHomePath, Config.detectMavenHome(emptyMap()))
        pathMock.close()
    }

    @Test
    fun `detectMavenHome should throw exception when no valid Maven home is found`() {
        val mockUserHomePath = mockPathFile(false)
        val pathMock = Mockito.mockStatic(Path::class.java)
        pathMock.`when`<Path> { Path.of(any(), any()) }.thenReturn(mockUserHomePath)
        assertThrows<IllegalStateException> { Config.detectMavenHome(emptyMap()) }
        pathMock.close()
    }

    private fun mockPathFile(exists: Boolean): Path {
        val mockUserHomePath = Mockito.mock(Path::class.java)
        val mockFile = Mockito.mock(File::class.java)
        Mockito.`when`(mockFile.exists()).thenReturn(exists)
        Mockito.`when`(mockUserHomePath.toFile()).thenReturn(mockFile)
        return mockUserHomePath
    }
}
