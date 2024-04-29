package com.aplus.batchtemplate

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus.COMPLETED
import org.springframework.batch.core.Job
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired

class MyJobTest(
    @Autowired
    private val jobLauncherTestUtils: JobLauncherTestUtils,
    @Autowired
    private val myJob: Job,
) : SpringBatchInitializer() {

    @Test
    fun myJobTest() {
        jobLauncherTestUtils.job = myJob
        val jobExecution = jobLauncherTestUtils.launchJob()
        jobExecution.exitStatus shouldBe COMPLETED
    }
}
