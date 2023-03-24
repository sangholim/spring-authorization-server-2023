package com.service.authorization.sessionManagement

import jakarta.persistence.*
import org.springframework.session.FlushMode
import java.util.*

/**
 * 세션 관리 테이블
 */
@Entity
class SessionManagement(
        /**
         * id
         */
        @Id
        val id: String,

        /**
         * session timeout (분)
         */
        var timeout: Long,

        /**
         * session 저장 방식
         */
        @Enumerated(EnumType.STRING)
        var flushMode: FlushMode
) {
    companion object {
        fun of(sessionTimeout: Long, flushMode: FlushMode): SessionManagement = SessionManagement(
                id = UUID.randomUUID().toString(),
                timeout = sessionTimeout,
                flushMode = flushMode
        )
    }
}