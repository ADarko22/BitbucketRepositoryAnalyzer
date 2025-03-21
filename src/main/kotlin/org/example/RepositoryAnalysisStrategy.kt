package org.example

import java.nio.file.Path

interface RepositoryAnalysisStrategy {
    fun analyzeRepo(repoDir: Path): List<String>
}