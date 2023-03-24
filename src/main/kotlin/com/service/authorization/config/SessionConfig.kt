package com.service.authorization.config

import com.service.authorization.sessionManagement.SessionManagementRepository
import com.service.authorization.sessionManagement.SessionManagementService
import org.springframework.boot.autoconfigure.session.RedisSessionProperties
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.data.redis.RedisSessionRepository

@Configuration
class SessionConfig(
        private val sessionManagementRepository: SessionManagementRepository,
        private val sessionRepository: RedisSessionRepository,
        private val serverProperties: ServerProperties,
        private val sessionProperties: RedisSessionProperties
) {

    @Bean
    fun SessionManagementService(): SessionManagementService {
        return SessionManagementService(sessionManagementRepository, sessionRepository, serverProperties.servlet.session, sessionProperties)
    }
}