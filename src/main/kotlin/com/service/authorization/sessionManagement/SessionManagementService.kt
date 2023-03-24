package com.service.authorization.sessionManagement

import com.service.authorization.sessionManagement.SessionManagement.Companion.sessionManagement
import org.springframework.boot.autoconfigure.session.RedisSessionProperties
import org.springframework.boot.web.servlet.server.Session
import org.springframework.session.data.redis.RedisSessionRepository
import java.time.Duration

class SessionManagementService(
        private val sessionManagementRepository: SessionManagementRepository,
        private val sessionRepository: RedisSessionRepository,
        session: Session,
        redisSessionProperties: RedisSessionProperties
) {
    init {
        val sessionManagement = sessionManagement {
            this.timeout = session.timeout.toMinutes()
            this.flushMode = redisSessionProperties.flushMode
        }
        if (sessionManagementRepository.count() == 0L) {
            sessionManagementRepository.save(sessionManagement)
        }
    }

    fun get(): SessionManagement = sessionManagementRepository.findAll().first()

    fun update(payload: SessionManagementUpdatePayload) {
        SessionManagement.from(get()).update(payload).build().run(sessionManagementRepository::save).run {
            sessionRepository.setFlushMode(this.flushMode)
            sessionRepository.setDefaultMaxInactiveInterval(Duration.ofMinutes(this.timeout))
        }
    }
}