package com.xinyi.appsettings.processor.create

import com.xinyi.appsettings.config.SourceFileData
import com.xinyi.appsettings.processor.AbstractProcessor

data class IgnoreFile(
    val ignoreFiles: String
) {
    val template = """
        *.cpp
        *.h
        *.cmake
        .gitignore
        $ignoreFiles
    """.trimIndent()
}

class CreateIgnore : AbstractProcessor() {

    var ignores = ""

    override fun getData(sourceFileData: SourceFileData): String {
        ignores += "*.properties\n"
        return IgnoreFile(ignores).template
    }
}