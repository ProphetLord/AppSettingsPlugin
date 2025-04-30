package com.xinyi.appsettings.processor.create

import com.xinyi.appsettings.config.SourceFileData
import com.xinyi.appsettings.config.DataCacheHandler
import com.xinyi.appsettings.config.DataConfig
import com.xinyi.appsettings.processor.AbstractProcessor

data class ClassH(
    val configClassName: String,
    val namespaceName: String,
    val dataConfig: DataConfig
) {
    val template = """
#pragma once

#include "${configClassName.lowercase()}.h"

${
    dataConfig.items.mapNotNull { item ->
        if (DataCacheHandler.cacheTypes.contains(item.type) || DataCacheHandler.cacheEnums.contains(item.type)) {
            "#inlucde \"${item.type.lowercase()}.h\""
        } else {
            null
        }
    }.joinToString("\n")
}

namespace $namespaceName {
    struct ${dataConfig.name}
    {
        static char const g[];

        ${
            dataConfig.items.joinToString("\n\t\t") { item ->
                "static $configClassName<${item.type}, g> ${item.name};"
            }
        }
        
        static void init();
        static void reset();
    };
}
    """.trimIndent()
}

class CreateClassHFile : AbstractProcessor() {

    override fun getData(sourceFileData: SourceFileData): String {
        val context = ClassH(
            sourceFileData.getConfigClassName(),
            sourceFileData.getNamespaceName(),
            sourceFileData.getDataConfig()
        ).template

        return context
    }
}