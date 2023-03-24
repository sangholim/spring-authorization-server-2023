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
        inline fun sessionManagement(block: Builder.() -> Unit) = Builder().apply(block).build()

        fun from(sessionManagement: SessionManagement): Builder = Builder().apply {
            this.id = sessionManagement.id
            this.timeout = sessionManagement.timeout
            this.flushMode = sessionManagement.flushMode
        }
    }


    private constructor(builder: Builder) : this(builder.id!!, builder.timeout, builder.flushMode)

    class Builder {
        var id: String? = UUID.randomUUID().toString()
        var timeout: Long = 0
        var flushMode: FlushMode = FlushMode.ON_SAVE

        fun build(): SessionManagement {
            return SessionManagement(this)
        }

        fun update(payload: SessionManagementUpdatePayload): Builder {
            this.timeout = payload.timeout
            this.flushMode = payload.flushMode
            return this
        }
    }
}