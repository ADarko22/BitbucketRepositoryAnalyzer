package io.github.adarko22.analyser

import io.github.adarko22.analyser.repo.RepositoriesAnalyser
import io.github.adarko22.bitbucket.BitbucketApiClient
import kotlinx.coroutines.runBlocking
import net.bytebuddy.asm.MemberSubstitution.Argument
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.*
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectsAnalyzerTest {

    private val bitbucketApiClient: BitbucketApiClient = mock()
    private val reposAnalyser: RepositoriesAnalyser = mock()
    private val projectsAnalyzer = ProjectsAnalyzer(reposAnalyser, bitbucketApiClient)
    private val project1 = "PROJ1"
    private val project2 = "PROJ2"
    private val projectKeys = listOf(project1, project2)
    private val reposCloneLinks = listOf("repo1_url", "repo2_url")

    @BeforeEach
    fun setUp() {
        reset(bitbucketApiClient, reposAnalyser)
        whenever(bitbucketApiClient.getProjectKeys()).thenReturn(projectKeys)
        whenever(bitbucketApiClient.getReposLinksForProjectKey(project1)).thenReturn(reposCloneLinks)
        whenever(bitbucketApiClient.getReposLinksForProjectKey(project2)).thenReturn(reposCloneLinks)
        whenever(reposAnalyser.analyseRepos(reposCloneLinks)).thenReturn(listOf())
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("inputs")
    fun `should analyse projects with correct interactions`(testCase: String, projectKeysToAnalyse: List<String>) {
        runBlocking {
            val result = projectsAnalyzer.analyzeAllProjectsAndGenerateReport(projectKeysToAnalyse)
            assertEquals(2, result.size)
            assertEquals(project1, result[0].projectKey)
            assertEquals(project2, result[1].projectKey)
        }

        verify(bitbucketApiClient).getReposLinksForProjectKey(project1)
        verify(bitbucketApiClient).getReposLinksForProjectKey(project2)
        verify(reposAnalyser, times(2)).analyseRepos(reposCloneLinks)
    }

    private fun inputs() = Stream.of(
        Arguments.arguments("All projects in Bitbucket", listOf<String>()),
        Arguments.arguments("Specified projects", projectKeys)
    )
}
