package com.service.authorization.registeredClient

import org.springframework.security.oauth2.server.authorization.settings.TokenSettings

/**
 * register-client token-settings 응답 데이터
 */
data class TokenSettingsView(
        /**
         * 승인 코드 생명 주기 (분)
         */
        val authorizationCodeTimeToLive: Int,
        /**
         * 액세스 토큰 생명주기 (분)
         */
        val accessTokenTimeToLive: Int,
        /**
         * 리프래쉬 토큰 생명주기 (분)
         */
        val refreshTokenTimeToLive: Int
)

fun TokenSettings.toView() = TokenSettingsView(
        authorizationCodeTimeToLive = authorizationCodeTimeToLive.toMinutes().toInt(),
        accessTokenTimeToLive = accessTokenTimeToLive.toMinutes().toInt(),
        refreshTokenTimeToLive = refreshTokenTimeToLive.toMinutes().toInt()
)