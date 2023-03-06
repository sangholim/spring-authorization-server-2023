package com.service.authorization.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.spring.SpringExtension
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

object ExtensionConfig: AbstractProjectConfig() {
    val postgresql = PostgreSQLContainer<Nothing>("postgres:11.1").apply {
        this.withUsername("root")
        this.withPassword("password")
        this.withDatabaseName("authorization")
    }

    val redis = GenericContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379)

    init {
        postgresql.start()
        redis.start()
        System.setProperty("spring.data.redis.host", redis.host)
        System.setProperty("spring.data.redis.port", redis.getMappedPort(6379).toString())
        System.setProperty("spring.data.redis.password", "")
        System.setProperty("spring.datasource.url", postgresql.jdbcUrl)
        System.setProperty("spring.datasource.username", postgresql.username)
        System.setProperty("spring.datasource.password", postgresql.password)
    }
    override fun extensions(): List<Extension> {
        return super.extensions().plus(SpringExtension)
    }
}