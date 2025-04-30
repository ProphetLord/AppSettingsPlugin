package com.xinyi.appsettings.processor.create

import com.xinyi.appsettings.config.DataConfig
import com.xinyi.appsettings.config.SourceFileData
import com.xinyi.appsettings.processor.AbstractProcessor

data class EnumsFile(
    val nameSpace: String,
    val dataConfig: DataConfig
) {
    val template = """
        #pragma once

        #include <qobject.h>
        #include <qdatastream.h>
        #include <qdebug.h>

        namespace $nameSpace {
            enum ${dataConfig.name} {
                ${
                    dataConfig.toNameList().joinToString("\n\t\t") { item ->
                        item 
                    }
                }
            };
        }

        Q_DECLARE_METATYPE($nameSpace::${dataConfig.name})

        inline QDataStream& operator<<(QDataStream &out, const $nameSpace::${dataConfig.name}& data) {
            return out << (int)data;
        }

        inline QDataStream& operator>>(QDataStream &in, $nameSpace::${dataConfig.name}& data) {
            int tmp;
            in >> tmp;
            data = $nameSpace::${dataConfig.name}(tmp);
            return in;
        }

        inline QDebug operator<<(QDebug debug, const $nameSpace::${dataConfig.name}& data) {
            QDebugStateSaver saver(debug);
            switch(data) {
                ${
                    dataConfig.toNameList().joinToString("\n\t\t") { item ->
                        "case $nameSpace::${dataConfig.name}::$item: debug.nospace() << \"$item\""
                    }
                }
            }
            return debug;
        }
    """.trimIndent()
}

class CreateEnumsFile : AbstractProcessor() {

    override fun getData(sourceFileData: SourceFileData): String {
        val context = EnumsFile(
            nameSpace = sourceFileData.getNamespaceName(),
            dataConfig = sourceFileData.getDataConfig()
        ).template
        return context
    }
}