package io.github.adarko22.analyser.repo

import io.github.adarko22.TestUtils
import io.github.adarko22.analyser.model.RepoAnalysisResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilterDependencyStrategyTest {

    @Test
    fun `should detect Tomcat dependencies in Maven project`() {
        val result = runAnalysisWithFilter(FilterDependencyRegex.TOMCAT, "maven-test-project")
        val expected = RepoAnalysisResult(
            repoName = "maven-test-project",
            analysisInfo = listOf(
                "org.apache.tomcat:tomcat-util:jar:9.0.94",
                "org.apache.tomcat:tomcat-juli:jar:9.0.94"
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should detect JUnit dependencies in Gradle project`() {
        val result = runAnalysisWithFilter(FilterDependencyRegex.JUNIT, "gradle-test-project")
        val expected = RepoAnalysisResult(
            repoName = "gradle-test-project",
            analysisInfo = listOf("junit:junit:4.13.2")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should return 'Not a Supported Project' for unsupported projects`() {
        val result = runAnalysisWithFilter(FilterDependencyRegex.SPRING, "unsupported-test-project")
        val expected = RepoAnalysisResult(
            repoName = "unsupported-test-project",
            analysisInfo = listOf("Not a Supported Project!")
        )
        assertEquals(expected, result)
    }

    private fun runAnalysisWithFilter(filterRegex: FilterDependencyRegex, projectName: String): RepoAnalysisResult {
        val projectDir = TestUtils.getTestResourcePath(projectName)
        val analysisStrategy = FilterDependencyStrategy(filterRegex.regex)
        return analysisStrategy.analyseRepo(projectDir)
    }
}
