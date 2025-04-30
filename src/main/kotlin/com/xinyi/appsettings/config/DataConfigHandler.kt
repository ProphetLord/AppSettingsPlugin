package com.xinyi.appsettings.config

import com.intellij.notification.NotificationType
import com.intellij.openapi.observable.properties.ObservableMutableProperty
import com.intellij.openapi.observable.properties.PropertyGraph
import com.xinyi.appsettings.notification.AppSettingsNotifications
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.Properties

class DataConfigHandler {

    companion object {
        @JvmStatic
        val propertyGraph = PropertyGraph()
        @JvmStatic
        var isAddr: ObservableMutableProperty<Boolean> = propertyGraph.property(false)

        @JvmStatic
        fun saveConfigData(path: String, name: String) {
            if (path.isEmpty()) {
                return
            }

            val filePath = "$path/${name}.properties"
            val props = Properties()
            props.setProperty("isAddr", isAddr.get().toString())
            try {
                OutputStreamWriter(FileOutputStream(filePath), Charsets.UTF_8).use { fos ->
                    props.store(fos, "Storing configuration files")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                e.message?.let { AppSettingsNotifications.showSimple(it, NotificationType.ERROR) }
            }
        }

        @JvmStatic
        fun loadConfigData(path: String, name: String) {
            if (path.isEmpty()) {
                return
            }

            val filePath = "$path/${name}.properties"
            val props = Properties()

            val file = File(filePath)
            if (!file.exists()) {
                return
            }

            try {
                FileInputStream(filePath).use { fos ->
                    props.load(fos)
                }

                val value = props.getProperty("isAddr")?.toBoolean() ?: false
                isAddr.set(value)
            } catch (e: Exception) {
                e.printStackTrace()
                e.message?.let { AppSettingsNotifications.showSimple(it, NotificationType.ERROR) }
            }
        }
    }
}