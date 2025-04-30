package com.xinyi.appsettings.actions;

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.xinyi.appsettings.dialog.SettingDialog
import com.xinyi.appsettings.notification.AppSettingsNotifications
import javax.xml.parsers.DocumentBuilderFactory

class PopupAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if(file != null) {
            val fileDir = file.parent.path
            if (file.extension.equals("xml", ignoreCase = true)) {  // 判断是否为xml文件
                val dbFactory = DocumentBuilderFactory.newInstance()
                val dBuilder = dbFactory.newDocumentBuilder()
                try {
                    val doc = dBuilder.parse(file.path)      // 尝试解析文件
                    val dialog = e.project?.let { SettingDialog(it) }
                    if(dialog == null) {
                        AppSettingsNotifications.showSimple("Dialog create error", NotificationType.ERROR)
                        return
                    }
                    dialog.setConfigDir(fileDir)
                    val res = dialog.setXmlDocument(doc)
                    if (res) {
                        val result = dialog.showAndGet()
                        AppSettingsNotifications.showSimple("Generate $result")
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                    AppSettingsNotifications.showSimple("Xml文件解析错误", NotificationType.ERROR)
                }
            } else {
                AppSettingsNotifications.showSimple("The current file format is not XML", NotificationType.ERROR)
            }
        } else {
            AppSettingsNotifications.showSimple("No files selected", NotificationType.ERROR)
        }
    }
}