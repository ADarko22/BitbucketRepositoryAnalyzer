# Bitbucket Repository Analyzer

This Kotlin project allows you to **clone repositories from Bitbucket and analyze them**.  
A repository can be analyzed according to
a [RepositoriesAnalyser](src/main/kotlin/io/github/adarko22/analyser/RepositoriesAnalyser.kt).

## 🚀 Features

- ✅ **Clones all repositories** from a specified Bitbucket URL →
  See [BitbucketApiClient](src/main/kotlin/io/github/adarko22/bitbucket/BitbucketApiClient.kt)
    - ✅ **Analyzes cloned repositories** according to a strategy →
      See [RepositoriesAnalyser](src/main/kotlin/io/github/adarko22/analyser/RepositoriesAnalyser.kt)

### Current Repository Analysis Strategies

- 📌 [TomcatRepositoryAnalysisStrategy](src/main/kotlin/io/github/adarko22/analyser/TomcatRepositoryAnalysisStrategy.kt)

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
`M2_HOME` or `MAVEN_HOME` environment variable. If you don't have Maven installed use sdk man to install it:

  ```sh
  sdk install maven 3.8.1
  sdk default maven 3.8.1
  ```

---

### 2️⃣ Configuration

In [Main](src/main/kotlin/io/github/adarko22/Main.kt), the following configuration values are required via environment
variables:

- **BITBUCKET_URL**: The URL of your Bitbucket instance (e.g., https://bitbucket.org).
- **BITBUCKET_USERNAME**: Your Bitbucket username.
- **BITBUCKET_PASSWORD**: Your Bitbucket password.

Ensure that your **Maven installation** is correctly configured:

- setting **M2_HOME** or **MAVEN_HOME** to the path of your Maven installation.
- or installing maven via **SDK Man**: `~/.sdkman/candidates/maven/current`

You may also configure additional options such as **repository filters** or **analysis strategy** (to be implemented).

---

## 📌 Running the Application

Once everything is set up, run the application using:

```sh
./gradlew run
```

Or, if running manually:

```sh
java -jar build/libs/bitbucket-repo-analyzer.jar
```

---

## 📊 Expected Output

The application prints:

1. **Progress logs** as it clones and analyzes each repository.
2. **A summary at the end**, which may take time depending on the number of repositories.

