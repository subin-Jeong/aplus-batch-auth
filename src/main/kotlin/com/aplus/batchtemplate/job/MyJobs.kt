package com.aplus.batchtemplate.job

import com.aplus.batchtemplate.config.ChunkSizeProperty
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.ListItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager

@Configuration
class MyJobs {
    @Bean
    fun myJob(
        jobRepository: JobRepository,
        myStep: Step,
    ) = JobBuilder("myJob", jobRepository)
        .start(myStep)
        .build()

    @Bean
    fun myStep(
        jobRepository: JobRepository,
        chunkSizeProperty: ChunkSizeProperty,
        transactionManager: DataSourceTransactionManager,
        pointHistoryReader: ListItemReader<Int>,
        pointHistoryWriter: ItemWriter<Int>,
    ) = StepBuilder("getOddStep", jobRepository)
        .chunk<Int, Int>(chunkSizeProperty.readSize, transactionManager)
        .reader(pointHistoryReader)
        .processor { if (it % 2 == 0) it else null }
        .writer(pointHistoryWriter)
        .build()

    @Bean
    fun pointHistoryReader() = ListItemReader(IntRange(1, 100_000).toList())

    @Bean
    fun pointHistoryWriter() = ItemWriter<Int> {
        for (i in it) {
            println(i)
        }
    }
}
