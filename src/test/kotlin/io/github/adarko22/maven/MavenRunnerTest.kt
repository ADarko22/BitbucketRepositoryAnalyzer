package io.github.adarko22.maven

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class MavenRunnerTest {

    @Test
    fun `runMavenDependencyTree should return dependency tree`() {
        val mavenRunner = MavenRunner()
        val repoDir = Paths.get(this.javaClass.classLoader.getResource("test-project")!!.toURI())

        val result = mavenRunner.runMavenDependencyTree(repoDir)

        assertTrue(result.any { it.contains("commons-lang:commons-lang:jar:2.6") })
        assertTrue(result.any { it.contains("junit:junit:jar:4.12") })
    }
}
