package com.xinyi.appsettings.processor.create

import com.xinyi.appsettings.config.SourceFileData
import com.xinyi.appsettings.processor.AbstractProcessor

data class AppSettingH (
    val nameSpace: String,
    val className: String,
    val iniFileName: String
) {
    val template = """
        #pragma once

        #include <qobject.h>

        #include "listener/iniproperty.h"

        namespace $nameSpace {
            template<typename T, char const *group>
            class $className : public IniProperty<T, group>
            {
                public:
                using IniProperty<T, group>::IniProperty;
                using IniProperty<T, group>::operator=;

                protected:
                QString iniFileName() override {
                    return "$iniFileName";
                }
            };

            void initProperties();
            void resetProperties();
        }
    """.trimIndent()
}

class CreateAppSettingHFile : AbstractProcessor() {

    var iniFileName = ""

    override fun getData(sourceFileData: SourceFileData): String {
        val context = AppSettingH(
            nameSpace = sourceFileData.getNamespaceName(),
            className = sourceFileData.getDataConfig().name,
            iniFileName = iniFileName
        ).template
        return context
    }
}