package io.github.adarko22.bitbucket

import org.eclipse.jgit.api.Git
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.File
import kotlin.io.path.createTempDirectory


private const val TEXT_FILE = "testfile.txt"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepoClonerTest {

    private lateinit var bareRepoDir: File
    private lateinit var repoCloner: RepoCloner

    @BeforeAll
    fun setUp() {
        repoCloner = RepoCloner("username", "password")
        bareRepoDir = createTempDirectory("temp-base-repo").toFile()
        Git.init().setDirectory(bareRepoDir).call()
        createInitialCommit(bareRepoDir)
    }

    @Test
    fun `cloneRepo should clone the repository successfully`() {
        val cloneDir = createTempDirectory("clone-repo").toFile()
        repoCloner.cloneRepo(bareRepoDir.toURI().toString(), cloneDir.toPath())
        assertTrue(cloneDir.resolve(TEXT_FILE).exists(), "Cloned repository should contain $TEXT_FILE")
    }

    private fun createInitialCommit(bareRepoDir: File) {
        val tempDir = createTempDirectory().toFile()
        Git.cloneRepository()
            .setURI(bareRepoDir.toURI().toString())
            .setDirectory(tempDir)
            .call().use { tempRepo ->
                val testFile = File(tempDir, TEXT_FILE)
                testFile.writeText("Initial content")
                tempRepo.add().addFilepattern(TEXT_FILE).call()
                tempRepo.commit().setMessage("Initial commit").call()
                tempRepo.push().setRemote("origin").call()
            }
    }
}
