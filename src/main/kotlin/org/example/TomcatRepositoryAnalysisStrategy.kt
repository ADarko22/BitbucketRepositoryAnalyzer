package org.example.org.example

import org.example.RepositoryAnalysisStrategy
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.pathString

class TomcatRepositoryAnalysisStrategy : RepositoryAnalysisStrategy {
    private val mavenRunner = MavenRunner()
    private val tomcatPattern = Regex("org.apache.tomcat[^:]*:\\S+:(\\d+\\.\\d+\\.\\d+)(?:\\.\\d+)?")

    override fun analyzeRepo(repoDir: Path): List<String> {
        if (isMavenProject(repoDir)) {
            println("Analyzing Dependency Tree for Tomcat dependencies:")
            val analysisResult = mavenRunner.runMavenDependencyTree(repoDir)
                .mapNotNull { line -> tomcatPattern.find(line)?.value }
                .distinct()
            if (analysisResult.isEmpty()) {
                println("\tNo results found")
                return analysisResult
            }
            println("\tFound the following results:")
            analysisResult.forEach { println("\t\t$it") }
            return analysisResult
        } else {
            println("\tNot a Maven project")
            return listOf("\tNot a Maven project")
        }
    }

    private fun isMavenProject(repoDir: Path) = Files.walk(repoDir, 1).anyMatch { it.fileName.pathString == "pom.xml" }
}
