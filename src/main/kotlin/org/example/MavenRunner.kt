package org.example.org.example

import org.apache.maven.cli.MavenCli
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Path

class MavenRunner {
    fun runMavenDependencyTree(repoDir: Path): List<String> {
        val out = ByteArrayOutputStream()
        val printStream = PrintStream(out)

        System.setProperty("maven.home", "~/.sdkman/candidates/maven/current")
        System.setProperty("maven.multiModuleProjectDirectory", repoDir.toString())

        val mavenCli = MavenCli()
        val args = arrayOf("-X", "-f", "${repoDir}/pom.xml", "dependency:tree")
        mavenCli.doMain(args, repoDir.toString(), printStream, System.err)

        return out.toString().lines()
    }
}


