package com.xinyi.appsettings.config

import com.xinyi.appsettings.processor.create.CreateAppSettingCppFile
import com.xinyi.appsettings.processor.create.CreateAppSettingHFile
import com.xinyi.appsettings.processor.create.CreateClassCppFile
import com.xinyi.appsettings.processor.create.CreateClassHFile
import com.xinyi.appsettings.processor.create.CreateCmake
import com.xinyi.appsettings.processor.create.CreateEnumsFile
import com.xinyi.appsettings.processor.create.CreateIgnore
import com.xinyi.appsettings.processor.create.CreateTypeFile
import org.w3c.dom.Element
import org.w3c.dom.NodeList

class IniConfig : Cloneable {

    var namespaceName = ""  // 命名空间名称

    var saveConfigFileName = ""   // 存储文件（ini）文件名

    var fileDir = ""    // 存储文件（ini）文件夹路径

    var fileVersion = ""    // 文件版本

    var cmakeName = ""      // cmake中的宏定义名称

    private val typeMap = mutableMapOf<String, DataConfig>()

    private val classMap = mutableMapOf<String, DataConfig>()

    private val enumMap = mutableMapOf<String, DataConfig>()

    fun setDir(dir: String) {
        fileDir = dir
    }

    private fun getDataConfig(node: Element): DataConfig {
        val typeNodeName = node.getAttribute("name")
        val addr = node.getAttribute("addr")

        val itemList = mutableListOf<ItemDataConfig>()
        val types = node.getElementsByTagName("item")
        for(j in 0 until types.length) {
            val itemNode = types.item(j) as Element

            val itemName = itemNode.getAttribute("name")
            val itemType = itemNode.getAttribute("type")
            val itemDefault = itemNode.getAttribute("default")
            val itemNote = itemNode.getAttribute("note")
            val itemInclude = itemNode.getAttribute("include")

            var config = ItemDataConfig(itemName, itemType, itemDefault, itemNote, itemInclude)
            itemList.add(config)
        }
        val dataConfig = DataConfig(typeNodeName, addr, itemList)
        return dataConfig
    }

    private fun getNodeValueDataConfig(node: Element): DataConfig {
        val typeNodeName = node.getAttribute("name")

        val itemList = mutableListOf<ItemDataConfig>()
        val types = node.getElementsByTagName("item")
        for(j in 0 until types.length) {
            val itemNode = types.item(j) as Element
            var itemName = itemNode.getAttribute("name")
            if(itemName.isEmpty()) {
                itemName = itemNode.textContent
            }
            val itemDefault = itemNode.getAttribute("default")

            itemList.add(ItemDataConfig(itemName, "", itemDefault, "", ""))
        }
        val dataConfig = DataConfig(typeNodeName, "", itemList)
        return dataConfig
    }

    fun insertTypeNodes(nodes: NodeList) {
        for(i in 0 until nodes.length ) {
            val typeNode = nodes.item(i) as Element
            val typeNodeName = typeNode.getAttribute("name")
            typeMap[typeNodeName] = getDataConfig(typeNode)
        }

    }

    fun insertClassNodes(nodes: NodeList) {
        for(i in 0 until nodes.length) {
            val classNode = nodes.item(i) as Element
            val classNodeName = classNode.getAttribute("name")
            classMap[classNodeName] = getDataConfig(classNode)
        }
    }

    fun insertEnumNodes(nodes: NodeList) {
        for(i in 0 until nodes.length) {
            val enumNode = nodes.item(i) as Element
            val enumNodeName = enumNode.getAttribute("name")
            enumMap[enumNodeName] = getNodeValueDataConfig(enumNode)
        }
    }

    fun processData() {
        DataCacheHandler.cacheEnums = enumMap.keys.toList()
        DataCacheHandler.cacheTypes = typeMap.keys.toList()
        DataCacheHandler.cacheClasses = classMap.keys.toList()

        // 创建appsettings.h
        val createAppSettingH = CreateAppSettingHFile()
        val configClass = if( namespaceName.split(".").first().last() == 's') {
            namespaceName.split(".").first() + "Config"
        } else {
            namespaceName.split(".").first() + "s"
        }
        val filePath = "$fileDir/${configClass.lowercase()}.h"

        val appFileData = SourceFileData(filePath, configClass, namespaceName,
            DataConfig(configClass, "", mutableListOf()))
        createAppSettingH.iniFileName = saveConfigFileName
        createAppSettingH.process(appFileData)

        // 创建appsettings.cpp
        val createAppSettingCpp = CreateAppSettingCppFile()
        val filePathCpp = "$fileDir/${configClass.lowercase()}.cpp"
        val appFileDataCpp = SourceFileData(filePathCpp, configClass, namespaceName,
            DataConfig(configClass, "", mutableListOf()))
        createAppSettingCpp.process(appFileDataCpp)

        if (!enumMap.isEmpty()) {
            enumMap.forEach { (key, value) ->
                val createEnums = CreateEnumsFile()
                val filePath = "$fileDir/${key.lowercase()}.h"
                val nodeData = SourceFileData(filePath, configClass, namespaceName, value)
                createEnums.process(nodeData)
            }
        }

        if (!typeMap.isEmpty()) {   // 创建type包含字段文件
            typeMap.forEach { (key, value) ->
                val createType = CreateTypeFile()
                val filePath = "$fileDir/${key.lowercase()}.h"
                val nodeData = SourceFileData(filePath, configClass, namespaceName, value)
                createType.process(nodeData)
            }
        }

        if(!classMap.isEmpty()) {   // 创建class包含字段文件
            classMap.forEach { (key, value) ->
                val createClassH = CreateClassHFile()
                val filePathH = "$fileDir/${key.lowercase()}.h"
                val nodeDataH = SourceFileData(filePathH, configClass, namespaceName, value)
                createClassH.process(nodeDataH)

                val createClassCpp = CreateClassCppFile()
                val filePathCpp = "$fileDir/${key.lowercase()}.cpp"
                val nodeDataCpp = SourceFileData(filePathCpp, configClass, namespaceName, value)
                createClassCpp.process(nodeDataCpp)
            }
        }

        // 创建.cmake
        val createCmake = CreateCmake()
        createCmake.cmakeName = cmakeName
        val cmakeFilePath = "$fileDir/${namespaceName.lowercase()}.cmake"
        val cmakeNode = SourceFileData(cmakeFilePath, configClass, namespaceName, DataConfig("", "", mutableListOf()))
        createCmake.process(cmakeNode)

        // 创建ignore
        val createIgnore = CreateIgnore()
        val ignoreFilePath = "$fileDir/.gitignore"
        val ignoreNodeData = SourceFileData(ignoreFilePath, configClass, namespaceName, DataConfig("", "", mutableListOf()))
        createIgnore.process(ignoreNodeData)
    }
}