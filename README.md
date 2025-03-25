# Bitbucket Repository Analyzer

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ADarko22_BitbucketRepositoryAnalyzer&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ADarko22_BitbucketRepositoryAnalyzer)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ADarko22_BitbucketRepositoryAnalyzer&metric=coverage)](https://sonarcloud.io/summary/new_code?id=ADarko22_BitbucketRepositoryAnalyzer)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=ADarko22_BitbucketRepositoryAnalyzer&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=ADarko22_BitbucketRepositoryAnalyzer)

This Kotlin project allows you to **clone repositories from Bitbucket and analyze them**.  
A repository can be analyzed according to
a [RepositoryAnalysisStrategy](src/main/kotlin/io/github/adarko22/analyser/repo/RepositoryAnalysisStrategy.kt).

## 🚀 Features

- ✅ **Clones all repositories** from a specified Bitbucket URL →
  See [BitbucketApiClient](src/main/kotlin/io/github/adarko22/bitbucket/BitbucketApiClient.kt)
- ✅ **Analyzes cloned repositories** according to a strategy →
  See [RepositoriesAnalyser](src/main/kotlin/io/github/adarko22/analyser/repo/RepositoryAnalysisStrategy.kt).

### Current Repository Analysis Strategies

- 📌 [FilterDependencyStrategy](src/main/kotlin/io/github/adarko22/analyser/repo/FilterDependencyStrategy.kt): Requires
  setting the `DEPENDENCY_REGEX` env variable.

---

## 🔧 Setup Instructions

### 1️⃣ Prerequisites

Before running the project, ensure you have the following installed:

#### 🔹 Install SDK Man (Optional)

Follow the instructions at https://sdkman.io/install/.

#### 🔹 Install Java 21+

This project requires **Java 21 or later**. If you don't have it installed use sdk man to install it:

  ```sh
  sdk install java 21.0.6-librca
  sdk default java 21.0.6-librca
  ```

#### 🔹 Install Maven

[MavenRunner](src/main/kotlin/io/github/adarko22/maven/MavenRunner.kt) **automatically detects Maven** using the
`M2_HOME` or `MAVEN_HOME` environment variable (see [Config](src/main/kotlin/io/github/adarko22/Config.kt)).
If you don't have Maven installed use sdk man to install it:

  ```sh
  sdk install maven 3.8.1
  sdk default maven 3.8.1
  ```

---

### 2️⃣ Configuration

The following configuration values are required via environment variables
(see [Config](src/main/kotlin/io/github/adarko22/Config.kt)):

- **BITBUCKET_URL**: The URL of your Bitbucket instance (e.g., https://bitbucket.org).
- **BITBUCKET_USERNAME**: Your Bitbucket username.
- **BITBUCKET_PASSWORD**: Your Bitbucket password.
- **PROJECT_KEYS**: Comma-separated list of proejct keys to analyse. By default, it fetches all projects.
- **DEPENDENCY_REGEX**: Regex for filtering the dependency on the analysed Maven Projects.
  E.g. `org.apache.tomcat[^:]*:\\S+:(\\d+\\.\\d+\\.\\d+)(?:\\.\\d+)?` for *org.apache.tomcat* dependencies (default).

Ensure that your **Maven installation** is correctly configured:

- setting **M2_HOME** or **MAVEN_HOME** to the path of your Maven installation.
- or installing maven via **SDK Man**: `~/.sdkman/candidates/maven/current`

You may also configure additional options such as **repository filters** or **analysis strategy** (to be implemented).

---

## 📌 Running the Application

Once everything is set up, run the application using:

```sh
export BITBUCKET_URL=<BITBUCKET_URL>
export BITBUCKET_USERNAME=<BITBUCKET_USERNAME>
export BITBUCKET_PASSWORD=<BITBUCKET_PASSWORD>
export PROJECT_KEYS=<PROJECT_KEYS_COMMA_SEPARATED_LIST>
./gradlew run
```

## 📊 Expected Output

The application prints:

1. **Progress logs** as it clones and analyzes each repository.
2. **A summary at the end**, which may take time depending on the number of repositories.
3. **A summary file** in the working directory: `analysis_report.json`

