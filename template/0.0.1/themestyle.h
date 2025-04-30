#pragma once

#include <qobject.h>
#include <qdatastream.h>
#include <qdebug.h>

namespace AppSetting {
    enum ThemeStyle {
        Dark,
		Light
    };
}

Q_DECLARE_METATYPE(AppSetting::ThemeStyle)

inline QDataStream& operator<<(QDataStream &out, const AppSetting::ThemeStyle& data) {
    return out << (int)data;
}

inline QDataStream& operator>>(QDataStream &in, AppSetting::ThemeStyle& data) {
    int tmp;
    in >> tmp;
    data = AppSetting::ThemeStyle(tmp);
    return in;
}

inline QDebug operator<<(QDebug debug, const AppSetting::ThemeStyle& data) {
    QDebugStateSaver saver(debug);
    switch(data) {
        case AppSetting::ThemeStyle::Dark: debug.nospace() << "Dark";
		case AppSetting::ThemeStyle::Light: debug.nospace() << "Light";
    }
    return debug;
}