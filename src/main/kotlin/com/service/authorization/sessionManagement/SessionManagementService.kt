package com.service.authorization.sessionManagement

import org.springframework.boot.autoconfigure.session.RedisSessionProperties
import org.springframework.boot.web.servlet.server.Session
import org.springframework.session.data.redis.RedisSessionRepository

class SessionManagementService(
        private val sessionManagementRepository: SessionManagementRepository,
        private val sessionRepository: RedisSessionRepository,
        session: Session,
        redisSessionProperties: RedisSessionProperties
) {
    init {
        val sessionManagement = SessionManagement.of(session.timeout.toMinutes(), redisSessionProperties.flushMode)
        sessionManagementRepository.save(sessionManagement)
    }

    fun get(): SessionManagement = sessionManagementRepository.findAll().first()
}