package com.xinyi.appsettings.processor.create

import com.xinyi.appsettings.config.DataCacheHandler
import com.xinyi.appsettings.config.DataConfig
import com.xinyi.appsettings.config.DataConfigHandler
import com.xinyi.appsettings.processor.AbstractProcessor
import com.xinyi.appsettings.config.SourceFileData

data class TypeFile(
    val nameSpace: String,
    val dataConfig: DataConfig,
    val memberList: String,
    val includeList: String
) {
    val template = """
#pragma once

#include <qobject.h>
#include <qdatastream.h>
#include <qdebug.h>

${
    DataCacheHandler.cacheEnums.joinToString("\n") { item ->
        "#include \"${item.lowercase()}.h\""
    }
}
$includeList

namespace $nameSpace {
    struct ${dataConfig.name}
    {
        ${
            if (DataConfigHandler.isAddr.get()) {
                """
                    enum { Addr = ${dataConfig.addr.takeIf { it.isNotEmpty() } ?: "0"} };
                """.trimIndent()
            } else {
                ""
            }
        }
        $memberList

        bool operator==(const ${dataConfig.name}& other) const {
            return ${
                dataConfig.toNameList().joinToString(" && \n\t\t\t") { item ->
                    "$item == other.$item"
                }
            };
        }

        bool operator!=(const ${dataConfig.name}& other) const {
            return !(*this == other);
        }
    };
}

inline QDataStream& operator<<(QDataStream &out, const $nameSpace::${dataConfig.name}& data) {
    return out ${
        dataConfig.toNameList().joinToString("\n" + " ".repeat(15)) { item ->
            "<< data.$item"
        }
    };
}

inline QDataStream& operator>>(QDataStream &in, $nameSpace::${dataConfig.name}& data) {
    return in ${
        dataConfig.toNameList().joinToString("\n" + " ".repeat(14)) { item ->
            ">> data.$item"
        }
    };
}

inline QDebug operator<<(QDebug debug, const $nameSpace::${dataConfig.name}& data) {
    QDebugStateSaver saver(debug);
    debug.nospace() << '{'
                    ${
                        dataConfig.toNameList().joinToString(" << \", \"\n" + " ".repeat(20)) { item ->
                            "<< \"'$item':\" << data.$item"
                        }
                    } << '}';
    return debug;
}

Q_DECLARE_METATYPE($nameSpace::${dataConfig.name})
    """.trimIndent()
}

class CreateTypeFile: AbstractProcessor() {

    override fun getData(sourceFileData: SourceFileData): String {
        val memberList = mutableListOf<String>()
        val includeString = mutableListOf<String>()
        for (item in sourceFileData.getDataConfig().items) {
            memberList.add(if (item.default.isEmpty()) {
                "${item.type} ${item.name};\t//${item.note}"
            } else {
                if (item.type == "QString" || item.type == "QByteArray") {
                    "${item.type} ${item.name} = \"${item.default}\";\t//${item.note}"
                } else {
                    "${item.type} ${item.name} = ${item.default};\t//${item.note}"
                }
            })

            if (item.includeDir.isNotEmpty()) {
                includeString.add("#include <${item.includeDir}>")
            }
        }

        return TypeFile(
            nameSpace = sourceFileData.getNamespaceName(),
            dataConfig = sourceFileData.getDataConfig(),
            memberList = memberList.joinToString("\n\t\t"),
            includeList = includeString.joinToString("\n")
        ).template
    }
}