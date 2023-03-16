package com.service.authorization.user

/**
 * 회원 응답 데이터
 */
class UserView(
        /**
         * id
         */
        val id: String,

        /**
         * email
         */
        val email: String,

        /**
         * 활성화 여부
         */
        val enabled: Boolean
)

fun User.toView() = UserView(
        id = id,
        email = email,
        enabled = enabled
)