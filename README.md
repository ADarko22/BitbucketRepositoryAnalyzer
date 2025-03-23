# Bitbucket Repository Analyzer

This Kotlin project allows you to **clone repositories from Bitbucket and analyze them**.  
A repository can be analyzed according to
a [RepositoryAnalysisStrategy](src/main/kotlin/org/example/RepositoryAnalysisStrategy.kt).

## 🚀 Features

- ✅ **Clones all repositories** from a specified Bitbucket URL →
  See [BitbucketApiClient](src/main/kotlin/org/example/BitbucketApiClient.kt)
- ✅ **Analyzes cloned repositories** according to a strategy →
  See [RepositoriesAnalyzer.kt](src/main/kotlin/org/example/RepositoriesAnalyzer.kt)

### Current Repository Analysis Strategies

- 📌 [TomcatRepositoryAnalysisStrategy](src/main/kotlin/org/example/TomcatRepositoryAnalysisStrategy.kt)

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

In [Main.kt](src/main/kotlin/org/example/Main.kt), provide **Bitbucket credentials**:

- **Bitbucket URL**
- **Username**
- **Password**

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

