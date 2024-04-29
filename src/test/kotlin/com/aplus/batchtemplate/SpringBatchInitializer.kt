package com.aplus.batchtemplate

import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBatchTest
@SpringBootTest
@Testcontainers
class SpringBatchInitializer {

    /**
     * test/resources/script.sql 파일을 읽어 초기화합니다.
     * 현재 2024.04.15일에 수정된 테이블 스키마입니다.
     */

    companion object {
        @Container
        @ServiceConnection
        val mariaDBContainer = mariaDBContainer()

        private fun mariaDBContainer(): MariaDBContainer<*> {
            val mariaDBContainer = MariaDBContainer(DockerImageName.parse("mariadb:latest"))
                .withDatabaseName("aplus_point")
                .withUsername("apluspoint")
                .withPassword("eoqkr1!")
                .withReuse(true)
                .withInitScript("script.sql")

            mariaDBContainer.portBindings = listOf("3306:3306")
            return mariaDBContainer
        }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.point-write.url") { mariaDBContainer.jdbcUrl }
            registry.add("spring.datasource.point-write.username") { mariaDBContainer.username }
            registry.add("spring.datasource.point-write.password") { mariaDBContainer.password }
            registry.add("spring.datasource.point-write.driver-class-name") { mariaDBContainer.driverClassName }
            registry.add("spring.datasource.point-read.url") { mariaDBContainer.jdbcUrl }
            registry.add("spring.datasource.point-read.username") { mariaDBContainer.username }
            registry.add("spring.datasource.point-read.password") { mariaDBContainer.password }
            registry.add("spring.datasource.point-read.driver-class-name") { mariaDBContainer.driverClassName }
        }
    }
}
