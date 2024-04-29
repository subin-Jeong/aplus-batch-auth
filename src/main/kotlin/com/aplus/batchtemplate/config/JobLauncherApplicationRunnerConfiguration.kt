package com.aplus.batchtemplate.config

import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(BatchProperties::class)
class JobLauncherApplicationRunnerConfiguration {

    /**
     * 스프링 애플리케이션을 실행하면 [JobLauncherApplicationRunner]가 실행되도록 설정하는 역할입니다.
     * [JobLauncherApplicationRunner]에는 `spring.batch.job.name` 프로퍼티가 설정되어 있으면 해당 `job`을 실행하도록 설정합니다.
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = ["enabled"], havingValue = "true", matchIfMissing = true)
    fun jobLauncherApplicationRunner(
        jobLauncher: JobLauncher,
        jobExplorer: JobExplorer,
        jobRepository: JobRepository,
        properties: BatchProperties
    ): JobLauncherApplicationRunner {
        val runner = JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository)
        val jobName: String? = properties.job.name
        if (!jobName.isNullOrEmpty()) {
            runner.setJobName(jobName)
        }
        return runner
    }
}
