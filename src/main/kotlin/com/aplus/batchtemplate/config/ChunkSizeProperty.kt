package com.aplus.batchtemplate.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "chunk")
data class ChunkSizeProperty(
    val writeSize: Int,
    val readSize: Int,
)
