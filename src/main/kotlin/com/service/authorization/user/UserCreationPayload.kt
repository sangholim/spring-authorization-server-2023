package com.service.authorization.user

/**
 * 회원 생성 필드 데이터
 */
data class UserCreationPayload(
        /**
         * 이메일
         */
        val email: String,

        /**
         * 비밀번호
         */
        val password: String
)
