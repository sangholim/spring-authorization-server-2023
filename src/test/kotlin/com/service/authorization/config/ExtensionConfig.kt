package com.service.authorization.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.spring.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer

class ExtensionConfig: AbstractProjectConfig() {
    val postgresql = PostgreSQLContainer<Nothing>("postgres:11.1").apply {
        this.withUsername("root")
        this.withPassword("password")
        this.withDatabaseName("authorization")
    }

    init {
        postgresql.start()
    }
    override fun extensions(): List<Extension> {
        return super.extensions().plus(SpringExtension)
    }
}