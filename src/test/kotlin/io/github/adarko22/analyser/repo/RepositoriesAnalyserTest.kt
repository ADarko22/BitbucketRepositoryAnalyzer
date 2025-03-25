package io.github.adarko22.analyser.repo

import io.github.adarko22.analyser.model.RepoAnalysisResult
import io.github.adarko22.bitbucket.RepoCloner
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import org.mockito.Mockito.*
import java.nio.file.Path

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepositoriesAnalyserTest {

    private val repoUrl = "https://example.com/scm/project/repo.git"
    private val repoName = "repo"

    private lateinit var strategy: RepositoryAnalysisStrategy
    private lateinit var repoCloner: RepoCloner
    private lateinit var repositoriesAnalyser: RepositoriesAnalyser
    private lateinit var repoDir: Path

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        strategy = mock(RepositoryAnalysisStrategy::class.java)
        repoCloner = mock(RepoCloner::class.java)
        repositoriesAnalyser = RepositoriesAnalyser(strategy, repoCloner, tempDir)
        repoDir = tempDir.resolve(repoName)
    }

    @Test
    fun `analyseRepos should handle successful analysis`() {
        val expectedResult = RepoAnalysisResult(repoName, listOf("Some Analysis Result"))
        `when`(strategy.analyseRepo(repoDir)).thenReturn(expectedResult)
        val results = repositoriesAnalyser.analyseRepos(listOf(repoUrl))
        assertTrue(results.repoAnalysisResults.contains(expectedResult))
        verify(repoCloner).cloneRepo(repoUrl, repoDir)
    }

    @Test
    fun `analyseRepos should handle clone failure gracefully`() {
        doThrow(RuntimeException("Clone failed")).`when`(repoCloner).cloneRepo(repoUrl, repoDir)
        val results = repositoriesAnalyser.analyseRepos(listOf(repoUrl))
        assertTrue(results.repoAnalysisResults.isEmpty())
    }
}
