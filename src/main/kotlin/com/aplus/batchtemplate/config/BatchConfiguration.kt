package com.aplus.batchtemplate.config

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class BatchConfiguration(
    private val batchDataSource: DataSource,
    private val batchTransactionManager: PlatformTransactionManager,
) : DefaultBatchConfiguration() {
    override fun getDataSource(): DataSource {
        return batchDataSource
    }

    override fun getTransactionManager(): PlatformTransactionManager {
        return batchTransactionManager
    }
}
