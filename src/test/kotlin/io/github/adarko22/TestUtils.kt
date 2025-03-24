package io.github.adarko22

import java.nio.file.Path
import java.nio.file.Paths

object TestUtils {

    fun getTestResourcePath(resourceName: String): Path {
        val resourceUrl = this.javaClass.classLoader.getResource(resourceName)
        requireNotNull(resourceUrl) { "Resource '$resourceName' not found" }
        return Paths.get(resourceUrl.toURI())
    }
}