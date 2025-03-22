package io.github.adarko22.bitbucket

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.slf4j.LoggerFactory
import java.nio.file.Path

class RepoCloner(username: String, password: String) {
    private val credentialsProvider = UsernamePasswordCredentialsProvider(username, password)
    private val logger = LoggerFactory.getLogger(RepoCloner::class.java)

    fun cloneRepo(repoUrl: String, targetDir: Path) {
        try {
            logger.info("Cloning repository: $repoUrl")
            Git.cloneRepository()
                .setURI(repoUrl)
                .setCredentialsProvider(credentialsProvider)
                .setDirectory(targetDir.toFile())
                .setCloneAllBranches(false)
                .setDepth(1)
                .call()
        } catch (e: GitAPIException) {
            logger.error("Git clone failed: ${e.message}")
        }
    }
}
