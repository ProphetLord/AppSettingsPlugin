#include "classconfig.h"

namespace AppSetting {
    char const ClassConfig::g[] = "ClassConfig";

    AppSettings<TypeConfig, ClassConfig::g> ClassConfig::typeConfig("typeConfig");
	AppSettings<ThemeStyle, ClassConfig::g> ClassConfig::theme("theme", ThemeStyle::Dark);
	AppSettings<qint64, ClassConfig::g> ClassConfig::test1("test1");
	AppSettings<QString, ClassConfig::g> ClassConfig::test2("test2", "Test2");
    
    void ClassConfig::init() {
        ClassConfig::typeConfig.init();
		ClassConfig::theme.init();
		ClassConfig::test1.init();
		ClassConfig::test2.init();
    }

    void ClassConfig::reset() {
        ClassConfig::typeConfig.resetDefault();
		ClassConfig::theme.resetDefault();
		ClassConfig::test1.resetDefault();
		ClassConfig::test2.resetDefault();
    }
}