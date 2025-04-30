#pragma once

#include <qobject.h>
#include <qdatastream.h>
#include <qdebug.h>
#include "themestyle"

namespace AppSetting {
    struct TypeConfig
    {
        
        bool config1 = false;
		int config2 = 1;
		qreal config3 = 1;

        bool operator==(const TypeConfig& other) const {
            return config1 == other.config1 && 
			config2 == other.config2 && 
			config3 == other.config3;
        }

        bool operator!=(const TypeConfig& other) const {
            return !(*this == other);
        }
    };
}

inline QDataStream& operator<<(QDataStream &out, const AppSetting::TypeConfig& data) {
    return out << config1
               << config2
               << config3;
}

inline QDataStream& operator>>(QDataStream &in, AppSetting::TypeConfig& data) {
    return in >> config1
              >> config2
              >> config3;
}

inline QDebug operator<<(QDebug debug, const AppSetting::TypeConfig& data) {
    QDebugStateSaver saver(debug);
    debug.nospace() << '{'
                    << "'config1':" << data.config1 << ", "
                    << "'config2':" << data.config2 << ", "
                    << "'config3':" << data.config3 << '}';
    return debug;
}

Q_DECLARE_METATYPE(AppSetting::TypeConfig)