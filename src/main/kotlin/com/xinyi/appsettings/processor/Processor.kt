package com.xinyi.appsettings.processor

import com.xinyi.appsettings.config.SourceFileData

public interface Processor {

    public fun process(sourceFileData: SourceFileData)
}