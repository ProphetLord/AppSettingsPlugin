#pragma once

#include "appsettings.h"

#inlucde "typeconfig.h"
#inlucde "themestyle.h"

namespace AppSetting {
    struct ClassConfig
    {
        static char const g[];

        static AppSettings<TypeConfig, g> typeConfig;
		static AppSettings<ThemeStyle, g> theme;
		static AppSettings<qint64, g> test1;
		static AppSettings<QString, g> test2;
        
        static void init();
        static void reset();
    };
}