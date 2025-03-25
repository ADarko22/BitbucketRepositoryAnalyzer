package io.github.adarko22

import io.github.adarko22.analyser.repo.FilterDependencyRegex
import java.nio.file.Path

data class Config(
    val bitbucketUrl: String,
    val username: String,
    val password: String,
    val projectKeys: List<String>,
    val dependencyRegex: Regex,
) {
    companion object {
        // Default environment variable keys
        const val BITBUCKET_URL = "BITBUCKET_URL"
        const val BITBUCKET_USERNAME = "BITBUCKET_USERNAME"
        const val BITBUCKET_PASSWORD = "BITBUCKET_PASSWORD"
        const val PROJECT_KEYS = "PROJECT_KEYS"
        const val DEPENDENCY_REGEX = "DEPENDENCY_REGEX"
        const val M2_HOME = "M2_HOME"
        const val MAVEN_HOME = "MAVEN_HOME"
        const val USER_HOME = "user.home"

        private const val MAVEN_HOME_NOT_DETECTED =
            "Maven home could not be detected. Please set the M2_HOME or MAVEN_HOME environment variable."

        fun load(env: Map<String, String> = System.getenv()): Config {
            val bitbucketUrl = env[BITBUCKET_URL] ?: errorLoadingEnvVariable(BITBUCKET_URL)
            val username = env[BITBUCKET_USERNAME] ?: errorLoadingEnvVariable(BITBUCKET_USERNAME)
            val password = env[BITBUCKET_PASSWORD] ?: errorLoadingEnvVariable(BITBUCKET_PASSWORD)
            val dependencyRegex = env[DEPENDENCY_REGEX]?.let { Regex(it) } ?: FilterDependencyRegex.TOMCAT.regex
            val projectKeys = (env[PROJECT_KEYS] ?: "").split(",").map { it.trim() }

            return Config(bitbucketUrl, username, password, projectKeys, dependencyRegex)
        }

        fun detectMavenHome(env: Map<String, String> = System.getenv()): Path {
            return env[M2_HOME]?.let { Path.of(it) }
                ?: env[MAVEN_HOME]?.let { Path.of(it) }
                ?: Path.of(System.getProperty(USER_HOME), ".sdkman/candidates/maven/current")
                    .takeIf { it.toFile().exists() }
                ?: throw IllegalStateException(MAVEN_HOME_NOT_DETECTED)
        }

        private fun errorLoadingEnvVariable(envVarKey: String): Nothing {
            throw IllegalStateException("Missing environment variable: $envVarKey")
        }
    }
}
