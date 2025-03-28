package io.github.adarko22.gradle

import io.github.adarko22.TestUtils
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.nio.file.Path

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GradleRunnerTest {

    @Test
    fun `runGradleMavenDependencyTree should return dependency tree`() {
        val gradleRunner = GradleRunner()

        val repoDir: Path = TestUtils.getTestResourcePath("gradle-test-project")
        val result = gradleRunner.runGradleMavenDependencyTree(repoDir)

        assertTrue(result[""]!!.any { it.contains("com.google.guava:guava:33.3.1-jre") })
        assertTrue(result["app"]!!.any { it.contains("com.squareup.okhttp3:okhttp:4.12.0") })
    }
}