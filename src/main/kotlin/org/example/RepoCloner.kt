package org.example.org.example

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.nio.file.Path

class RepoCloner(private val username: String, private val password: String) {

    val credentialsProvider = UsernamePasswordCredentialsProvider(username, password)

    fun cloneRepo(repoUrl: String, targetDir: Path) {
        try {
            println("Cloning repository: $repoUrl")
            Git.cloneRepository()
                .setURI(repoUrl)
                .setCredentialsProvider(credentialsProvider)
                .setDirectory(targetDir.toFile())
                .setCloneAllBranches(false)
                .setDepth(1)
                .call()
        } catch (e: GitAPIException) {
            println("Git clone failed: ${e.message}")
        }
    }
}
