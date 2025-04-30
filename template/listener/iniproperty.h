#pragma once

#include "observerproperty.h"

#include <qsettings.h>
#include <qstandardpaths.h>
#include <qdir.h>

template<typename T, char const* group>
class IniProperty : public ObserverProperty<T> {
public:
    using ObserverProperty<T>::ObserverProperty;
    using ObserverProperty<T>::operator=;

protected:
    void saveData(const QString& key, const T& value) override {
        QSettings settings(getSavedFilePath(), QSettings::IniFormat);
        settings.beginGroup(group);
        settings.setValue(key, getSupportedValue<T>(value));
        settings.endGroup();
    }

    T readData(const QString& key, const T& defaultValue) override {
        QSettings settings(getSavedFilePath(), QSettings::IniFormat);
        settings.beginGroup(group);
        auto data = settings.value(key, getSupportedValue<T>(defaultValue));
        settings.endGroup();
        return data.template value<T>();
    }

    template<typename E>
    static QVariant getSupportedValue(const typename std::enable_if<std::is_constructible<QVariant, E>::value, E>::type& v) {
        return v;
    }

    template<typename E>
    static QVariant getSupportedValue(const typename std::enable_if<!std::is_constructible<QVariant, E>::value, E>::type& v) {
        return QVariant::fromValue(v);
    }

    QString getSavedFilePath() {
        static auto filePath = QStandardPaths::writableLocation(QStandardPaths::AppDataLocation);
        QDir dir(filePath);
        if (!dir.exists()) {
            dir.mkdir(filePath);
        }
        return filePath + "/" + iniFileName();
    }

    virtual QString iniFileName() = 0;
};