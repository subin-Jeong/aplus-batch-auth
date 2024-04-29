package com.aplus.batchtemplate.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "script")
data class DataSourceScriptsProperty(
    val schemas: Array<String>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataSourceScriptsProperty

        return schemas.contentEquals(other.schemas)
    }

    override fun hashCode(): Int {
        return schemas.contentHashCode()
    }
}
