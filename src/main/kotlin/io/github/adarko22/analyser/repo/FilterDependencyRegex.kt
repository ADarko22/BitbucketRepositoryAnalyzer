package io.github.adarko22.analyser.repo


enum class FilterDependencyRegex(val regex: Regex) {
    TOMCAT(Regex("""org\.apache\.tomcat[^:]*(:\S+)?:([\d.]+)""")),
    JUNIT(Regex("""junit:junit(:\S+)?:([\d.]+)""")),
    SPRING(Regex("""org\.springframework:spring-core(:\S+)?:([\d.]+)"""));
}