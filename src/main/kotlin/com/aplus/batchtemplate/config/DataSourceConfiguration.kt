package com.aplus.batchtemplate.config

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration(
    val dataSourceScriptsProperty: DataSourceScriptsProperty,
    val writerDataSourceProperty: PointWriteDataSourceProperty,
    val readDataSourceProperty: PointReadDataSourceProperty,
) {

    @Bean
    @Primary
    fun batchTransactionManager(batchDataSource: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(batchDataSource)
    }

    @Bean
    @Primary
    fun batchDataSource(): DataSource {
        return EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScripts(*dataSourceScriptsProperty.schemas)
            .build()
    }

    @Bean
    fun pointReaderDataSource(): DataSource {
        return DataSourceBuilder.create()
            .url(readDataSourceProperty.url)
            .username(readDataSourceProperty.username)
            .password(readDataSourceProperty.password)
            .driverClassName(readDataSourceProperty.driverClassName)
            .build()
    }

    @Bean
    fun pointWriterDataSource(): DataSource {
        return DataSourceBuilder.create()
            .url(writerDataSourceProperty.url)
            .username(writerDataSourceProperty.username)
            .password(writerDataSourceProperty.password)
            .driverClassName(writerDataSourceProperty.driverClassName)
            .build()
    }
}
