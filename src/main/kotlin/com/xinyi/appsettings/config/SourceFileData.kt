package com.xinyi.appsettings.config

class SourceFileData(savePath: String, cName: String, nName: String, config: DataConfig) {
    private val saveFileName: String = savePath         // 文件名称（包含文件路径）用于存储文件
    private val configClassName: String = cName
    private val namespaceName: String = nName
    private val dataConfig: DataConfig = config


    fun getSaveFileName(): String {
        return saveFileName
    }

    fun getConfigClassName(): String {
        return configClassName
    }

    fun getNamespaceName(): String {
        return namespaceName
    }

    fun getDataConfig(): DataConfig {
        return dataConfig
    }
}