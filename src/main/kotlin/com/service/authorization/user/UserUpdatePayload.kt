package com.service.authorization.user

/**
 * 회원 업데이트 필드 데이터
 */
data class UserUpdatePayload(
        val email: String,
        val enabled: Boolean
)