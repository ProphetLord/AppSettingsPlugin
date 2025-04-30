package com.xinyi.appsettings.config

import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.Writer

object SyncConfigurations {

    @JvmStatic
    fun getWrite(name: String) : Writer {
        val file = File(name)
        val bufferedWriter = BufferedWriter(OutputStreamWriter(FileOutputStream(file), "utf-8"))
        return bufferedWriter
    }
}