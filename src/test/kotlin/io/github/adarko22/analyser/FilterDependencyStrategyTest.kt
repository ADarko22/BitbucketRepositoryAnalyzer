package io.github.adarko22.analyser

import io.github.adarko22.TestUtils
import io.github.adarko22.analyser.model.RepoAnalysisResult
import io.github.adarko22.maven.MavenRunner
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Path

class FilterDependencyStrategyTest {

    private lateinit var mavenRunner: MavenRunner
    private lateinit var analysisStrategy: FilterDependencyStrategy

    @BeforeEach
    fun setUp() {
        mavenRunner = MavenRunner()
        analysisStrategy = FilterDependencyStrategy(FilterDependencyRegex.TOMCAT.regex, mavenRunner)
    }

    @Test
    fun `analyseRepo should detect Tomcat dependencies in a Maven project`() {
        val mavenProjectDir: Path = TestUtils.getTestResourcePath("maven-test-project")

        val result = analysisStrategy.analyseRepo(mavenProjectDir)

        // Then: The result should contain Tomcat dependencies
        val expected = RepoAnalysisResult(
            repoName = "maven-test-project",
            analysisInfo = listOf("org.apache.tomcat:tomcat-catalina:jar:9.0.0")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `analyseRepo should return 'Not a Maven Project' for non-Maven projects`() {
        val nonMavenProjectDir: Path = TestUtils.getTestResourcePath("non-maven-test-project")

        val result = analysisStrategy.analyseRepo(nonMavenProjectDir)

        // Then: The result should indicate it's not a Maven project
        val expected = RepoAnalysisResult(
            repoName = "non-maven-test-project",
            analysisInfo = listOf("Not a Maven Project")
        )
        assertEquals(expected, result)
    }
}
