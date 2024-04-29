package com.aplus.batchtemplate

import com.aplus.batchtemplate.config.ChunkSizeProperty
import com.aplus.batchtemplate.config.DataSourceScriptsProperty
import com.aplus.batchtemplate.config.PointReadDataSourceProperty
import com.aplus.batchtemplate.config.PointWriteDataSourceProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    DataSourceScriptsProperty::class,
    PointReadDataSourceProperty::class,
    PointWriteDataSourceProperty::class,
    ChunkSizeProperty::class
)
class BatchTemplateApplication

fun main(args: Array<String>) {
    runApplication<BatchTemplateApplication>(*args)
}
