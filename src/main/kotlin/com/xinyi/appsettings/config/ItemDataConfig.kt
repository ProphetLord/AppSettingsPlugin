package com.xinyi.appsettings.config

data class ItemDataConfig (
    var name : String,
    var type : String,
    var default: String,
    var note : String,

    // TODO:以下属性需要在对话框中设置才能生效,待开发
    var includeDir: String,
)