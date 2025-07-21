package com.xinyi.appsettings.config

data class DataConfig(
    var name : String,      // 类名称
    var addr : String,
    var items : MutableList<ItemDataConfig>
) {
    fun toNameList(): List<String> {
        val list = mutableListOf<String>()
        items.forEach { config ->
            list.add(config.name)
        }
        return list
    }

    fun toItemValues(): List<String> {
        val list = mutableListOf<String>()
        items.forEach { config ->
            if (config.default.isNotEmpty())  {
                list.add(config.name + " = " + config.default)
            } else {
                list.add(config.name)
            }
        }
        return list
    }
}