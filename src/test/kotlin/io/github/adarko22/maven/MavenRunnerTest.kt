package io.github.adarko22.maven

import io.github.adarko22.TestUtils
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Path

class MavenRunnerTest {

    @Test
    fun `runMavenDependencyTree should return dependency tree`() {
        val mavenRunner = MavenRunner()
        val repoDir: Path = TestUtils.getTestResourcePath("maven-test-project")

        val result = mavenRunner.runMavenDependencyTree(repoDir)

        assertTrue(result.any { it.contains("commons-lang:commons-lang:jar:2.6") })
        assertTrue(result.any { it.contains("junit:junit:jar:4.12") })
    }
}
