package io.github.adarko22.maven

import org.apache.maven.cli.MavenCli
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Path

class MavenRunner {

    private val logger = LoggerFactory.getLogger(MavenRunner::class.java)

    fun runMavenDependencyTree(repoDir: Path): List<String> {
        val out = ByteArrayOutputStream()
        val printStream = PrintStream(out)

        System.setProperty("maven.home", "~/.sdkman/candidates/maven/current")
        System.setProperty("maven.multiModuleProjectDirectory", repoDir.toString())

        val mavenCli = MavenCli()
        val args = arrayOf("-X", "-f", "${repoDir}/pom.xml", "dependency:tree")

        logger.debug("Running `mvn ${args.joinToString { " " }}`")
        mavenCli.doMain(args, repoDir.toString(), printStream, System.err)

        return out.toString().lines()
    }
}


