package io.github.adarko22.analyser

import io.github.adarko22.analyser.model.RepoAnalysisResult
import java.nio.file.Path

interface RepositoryAnalysisStrategy {
    fun analyseRepo(repoDir: Path): RepoAnalysisResult
}

