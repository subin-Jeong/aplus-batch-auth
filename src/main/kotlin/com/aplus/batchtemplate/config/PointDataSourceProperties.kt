package com.aplus.batchtemplate.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.datasource.point-write")
data class PointWriteDataSourceProperty(
    val url: String,
    val username: String,
    val password: String,
    val driverClassName: String,
)

@ConfigurationProperties(prefix = "spring.datasource.point-read")
data class PointReadDataSourceProperty(
    val url: String,
    val username: String,
    val password: String,
    val driverClassName: String,
)
