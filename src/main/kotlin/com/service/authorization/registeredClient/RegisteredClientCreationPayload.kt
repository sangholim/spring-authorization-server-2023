package com.service.authorization.registeredClient

/**
 * register client 생성 payload
 */
data class RegisteredClientCreationPayload(
        /**
         * client-id
         */
        val clientId: String,
        /**
         * client-secret
         */
        val clientSecret: String?,
        /**
         * 인증 방식
         */
        val clientAuthenticationMethod: String,
        /**
         * 승인 타입
         */
        val authorizationGrantTypes: Set<String> = mutableSetOf(),
        /**
         * redirect-url 리스트
         */
        val redirectUris: Set<String> = mutableSetOf(),
        /**
         * 접근 가능한 회원 범위를 지정
         */
        val scopes: Set<String> = mutableSetOf(),
)