package com.xinyi.appsettings.processor.create

import com.xinyi.appsettings.config.SourceFileData
import com.xinyi.appsettings.config.DataCacheHandler
import com.xinyi.appsettings.processor.AbstractProcessor

data class AppSettingCpp(
    val nameSpace: String,
    val configName: String
) {
    val template = """
#include "${configName.lowercase()}.h"

${
    DataCacheHandler.cacheTypes.joinToString("\n") { item ->
        "#include \"${item.lowercase()}.h\""
    }
}

${
    DataCacheHandler.cacheClasses.joinToString("\n") { item ->
        "#include \"${item.lowercase()}.h\""
    }
}

namespace $nameSpace {
    void initProperties() {
        ${
            DataCacheHandler.cacheTypes.joinToString("\n\t\t") { item ->
                "qRegisterMetaType<${item}>();\n\t\t" + 
                "qRegisterMetaTypeStreamOperators<${item}>();"
            }
        }
        
        ${
            DataCacheHandler.cacheClasses.joinToString("\n\t\t") { item ->
                "${item}::init();"
            }
        }
    }

    void resetProperties() {
        ${
            DataCacheHandler.cacheClasses.joinToString("\n\t\t") { item ->
                "${item}::reset();"
            }
        }
    }
}
    """.trimIndent()
}

class CreateAppSettingCppFile : AbstractProcessor() {

    override fun getData(sourceFileData: SourceFileData): String {
        val context = AppSettingCpp(
            sourceFileData.getNamespaceName(),
            sourceFileData.getConfigClassName()
        ).template

        return context
    }

}