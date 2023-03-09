package com.service.authorization.registeredClient

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations


@Configuration
class RegisteredClientConfig {

    @Bean
    fun registeredClientRepository(jdbcOperations: JdbcOperations): CustomRegisteredClientRepository? {
        return CustomRegisteredClientRepository(jdbcOperations)
    }
}