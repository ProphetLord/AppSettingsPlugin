#include "appsettings.h"

#include "typeconfig.h"

#include "classconfig.h"

namespace AppSetting {
    void initProperties() {
        qRegisterMetaType<TypeConfig>();
		qRegisterMetaTypeStreamOperators<TypeConfig>();
        
        ClassConfig::init();
    }

    void resetProperties() {
        ClassConfig::reset();
    }
}