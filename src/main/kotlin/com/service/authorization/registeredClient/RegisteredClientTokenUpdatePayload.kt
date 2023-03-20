package com.service.authorization.registeredClient

/**
 * register-client-token 수정 필드 데이터
 */
data class RegisteredClientTokenUpdatePayload(
        /**
         * 승인 코드 생명 주기 (분)
         */
        val authorizationCodeTimeToLive: Long,
        /**
         * 액세스 토큰 생명주기 (분)
         */
        val accessTokenTimeToLive: Long,
        /**
         * 리프래쉬 토큰 생명주기 (분)
         */
        val refreshTokenTimeToLive: Long
)
