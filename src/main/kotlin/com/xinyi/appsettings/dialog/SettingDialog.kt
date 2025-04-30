package com.xinyi.appsettings.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.xinyi.appsettings.AppSettingsBundle
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.rml.dfa.utils.toInt
import com.xinyi.appsettings.config.DataConfigHandler
import com.xinyi.appsettings.config.IniConfig
import com.xinyi.appsettings.notification.AppSettingsNotifications
import org.w3c.dom.Document
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton

import javax.swing.JComponent
import javax.swing.JPanel

class SettingDialog(val project: Project) : DialogWrapper(project, true) {

    private val comboBoxMap = mutableMapOf<String, ComboBox<String>>()

    private val config = IniConfig()

    init {
        title = AppSettingsBundle.message("setting.dialog.title")
        init()
    }

    override fun createCenterPanel(): JComponent {
        val pane = JPanel()
        pane.minimumSize = Dimension(380, 120)
        pane.layout = BoxLayout(pane, BoxLayout.PAGE_AXIS)

        val comboBoxData = mapOf(
            "Type类设置Addr" to arrayOf("false", "true")
        )

        comboBoxData.forEach { (label, items) ->
            val subPane = JPanel()
            subPane.maximumSize = Dimension(Int.MAX_VALUE, 24)
            subPane.layout = BoxLayout(subPane, BoxLayout.LINE_AXIS)

            subPane.border = BorderFactory.createTitledBorder(label)
            val lineComboBox = ComboBox(items)
            comboBoxMap[label] = lineComboBox

            subPane.add(lineComboBox)

            pane.add(subPane)
        }

        pane.add(Box.createVerticalGlue())
        return pane
    }

    override fun createSouthPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        val button = JButton("生成")
        button.addActionListener {
            DataConfigHandler.isAddr.set( when (comboBoxMap["Type类设置Addr"]?.selectedItem) {
                "true" -> true
                "false" -> false
                else -> false
            })

            config.processData()
            val configName: String = config.saveConfigFileName.split(".").first()
            DataConfigHandler.saveConfigData(config.fileDir, configName)
            close(OK_EXIT_CODE)
        }

        panel.add(button, BorderLayout.EAST)

        return panel
    }

    fun setConfigDir(dir: String) {
        config.setDir(dir)
    }

    fun setXmlDocument(doc: Document): Boolean {
        doc.documentElement.normalize()
        val root =  doc.documentElement
        if(root.nodeName == "settings") {
            val fileName = root.getAttribute("ini")
            val nName = root.getAttribute("namespace")
            val cmakeName = root.getAttribute("cmakeName")
            val version = root.getAttribute("version")
            if(!fileName.isEmpty() && !nName.isEmpty() && !cmakeName.isEmpty()) {   // 创建ini文件
                config.saveConfigFileName = fileName
                config.namespaceName = nName
                config.cmakeName = cmakeName
                config.fileVersion = version

                val configName: String = fileName.split(".").first()
                DataConfigHandler.loadConfigData(config.fileDir, configName)
                initDialogData()

                val typeNodes = root.getElementsByTagName("type")
                config.insertTypeNodes(typeNodes)
                val classNodes = root.getElementsByTagName("class")
                config.insertClassNodes(classNodes)
                val enumNodes = root.getElementsByTagName("enums")
                config.insertEnumNodes(enumNodes)

            } else {
                if(fileName.isEmpty()) {
                    AppSettingsNotifications.showSimple("Not set: ini")
                } else if (nName.isEmpty()) {
                    AppSettingsNotifications.showSimple("Not set: namespace")
                } else if (cmakeName.isEmpty()) {
                    AppSettingsNotifications.showSimple("Not set: cmakeName")
                }
                return false
            }
        } else {
            println("root is not settings")
            return false
        }
        return true
    }

    private fun initDialogData() {
        comboBoxMap["Type类设置Addr"]?.selectedIndex = DataConfigHandler.isAddr.get().toInt()
    }
}