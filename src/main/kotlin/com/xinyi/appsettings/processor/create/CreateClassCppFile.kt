package com.xinyi.appsettings.processor.create

import com.xinyi.appsettings.config.DataCacheHandler
import com.xinyi.appsettings.config.DataConfig
import com.xinyi.appsettings.config.SourceFileData
import com.xinyi.appsettings.processor.AbstractProcessor

data class ClassCpp(
    val nameSpace: String,
    val configName: String,
    val dataConfig: DataConfig
) {
    val template = """
#include "${dataConfig.name.lowercase()}.h"

namespace $nameSpace {
    char const ${dataConfig.name}::g[] = "${dataConfig.name}";

    ${
        dataConfig.items.joinToString("\n\t") { item ->
            if (item.default.isEmpty()) {
                "$configName<${item.type}, ${dataConfig.name}::g> ${dataConfig.name}::${item.name}(\"${item.name}\");"
            } else {
                "$configName<${item.type}, ${dataConfig.name}::g> ${dataConfig.name}::${item.name}(\"${item.name}\", ${item.default});"
            }
        }
    }
    
    void ${dataConfig.name}::init() {
        ${
            dataConfig.items.joinToString("\n\t\t") { item ->
                "${dataConfig.name}::${item.name}.init();"
            }
        }
    }

    void ${dataConfig.name}::reset() {
        ${
            dataConfig.items.joinToString("\n\t\t") { item ->
                "${dataConfig.name}::${item.name}.resetDefault();"
            }
        }
    }
}
    """.trimIndent()
}

class CreateClassCppFile : AbstractProcessor() {

    override fun getData(sourceFileData: SourceFileData): String {
        val context = ClassCpp(
            nameSpace = sourceFileData.getNamespaceName(),
            configName = sourceFileData.getConfigClassName(),
            dataConfig = sourceFileData.getDataConfig()
        ).template
        return context
    }
}