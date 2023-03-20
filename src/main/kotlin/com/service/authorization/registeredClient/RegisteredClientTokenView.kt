package com.service.authorization.registeredClient

import org.springframework.security.oauth2.server.authorization.settings.TokenSettings

/**
 * register-client token-settings 응답 데이터
 */
data class RegisteredClientTokenView(
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

fun TokenSettings.toView() = RegisteredClientTokenView(
        authorizationCodeTimeToLive = authorizationCodeTimeToLive.toMinutes(),
        accessTokenTimeToLive = accessTokenTimeToLive.toMinutes(),
        refreshTokenTimeToLive = refreshTokenTimeToLive.toMinutes()
)