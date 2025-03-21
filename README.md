# Bitbucket Repository Analyzer

This Kotlin project allows you to **clone repositories from Bitbucket and analyze them**. 
A repository can be analzed according to a [RepositoryAnalysisStrategy](src/main/kotlin/org/example/RepositoryAnalysisStrategy.kt).

## Features:
- Clones all repositories from a specified Bitbucket URL. See [BitbucketApiClient](src/main/kotlin/org/example/BitbucketApiClient.kt)
- Analyzes all the cloned repositories according to a Strategy. See [RepositoriesAnalyzer.kt](src/main/kotlin/org/example/RepositoriesAnalyzer.kt)

### Current Repository Analysis Strategies:
- [TomcatRepositoryAnalysisStrategy](src/main/kotlin/org/example/TomcatRepositoryAnalysisStrategy.kt)

## Setup Instructions

### 1. Prerequisites

Before running the project, ensure you have the following installed:

- **SDKMAN**: The [MavenRunner.kt](src/main/kotlin/org/example/MavenRunner.kt) has the `maven.home` hardcoded to `~/.sdkman/candidates/maven/current`
  - Follow instruction at https://sdkman.io/install/ 
- **Maven 3.8.1**: To run the `mvn dependency:tree` command
  - Install with: `sdk install maven 3.8.1`
  - Set as current: `sdk default maven 3.8.1

### 2. Configuration
In the [Main.kt](src/main/kotlin/org/example/Main.kt) file, provide credentials to access Bitbucket: url, username and password.

## Output
The application prints to the console the progress on each repository cloned and analyzed.
The application prints also a summary at the end (it may take a long time and a powerful machine).

