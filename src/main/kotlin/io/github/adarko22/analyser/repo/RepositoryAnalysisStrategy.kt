package io.github.adarko22.analyser.repo

import io.github.adarko22.analyser.model.RepoAnalysisResult
import java.nio.file.Path

fun interface RepositoryAnalysisStrategy {
    fun analyseRepo(repoDir: Path): RepoAnalysisResult
}

