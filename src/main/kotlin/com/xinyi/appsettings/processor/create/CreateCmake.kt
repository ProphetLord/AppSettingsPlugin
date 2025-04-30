package com.xinyi.appsettings.processor.create

import com.xinyi.appsettings.config.DataCacheHandler
import com.xinyi.appsettings.config.DataConfig
import com.xinyi.appsettings.config.SourceFileData
import com.xinyi.appsettings.processor.AbstractProcessor

data class CmakeFile(
    val cmakeName: String,
    val configName: String
) {
    val template = """
set($cmakeName
    ${
        DataCacheHandler.cacheTypes.joinToString("\n\t") { item->
            "\${CMAKE_CURRENT_LIST_DIR}/${item.lowercase()}.h"
        }
    }
    ${
        DataCacheHandler.cacheClasses.joinToString("\n\t") { item ->
            "\${CMAKE_CURRENT_LIST_DIR}/${item.lowercase()}.h"
        }
    }
    ${configName}.h

    ${
        DataCacheHandler.cacheClasses.joinToString("\n\t") { item ->
            "\${CMAKE_CURRENT_LIST_DIR}/${item.lowercase()}.cpp"
        }
    }
    ${configName}.cpp
)

include_directories
    """.trimIndent() + "(\$CMAKE_CURRENT_LIST_DIR})"
}

class CreateCmake : AbstractProcessor() {

    var cmakeName: String = ""

    override fun getData(sourceFileData: SourceFileData): String {
        val config = "\${CMAKE_CURRENT_LIST_DIR}/${sourceFileData.getConfigClassName().lowercase()}"
        return CmakeFile(cmakeName, config).template
    }
}