package com.xinyi.appsettings.processor

import com.xinyi.appsettings.config.SourceFileData
import com.xinyi.appsettings.config.SyncConfigurations

import java.io.Writer

public abstract class AbstractProcessor : Processor {

    protected abstract fun getData(sourceFileData: SourceFileData): String

    final fun getWriter(sourceFileData: SourceFileData): Writer {
        return SyncConfigurations.getWrite(sourceFileData.getSaveFileName())
    }

    final override fun process(sourceFileData: SourceFileData) {
        val string = getData(sourceFileData)
        val writer = getWriter(sourceFileData)
        writer.write(string)
        writer.close()
    }
}