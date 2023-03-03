package com.service.authorization.user

/**
 * 유저 DTO
 */
data class UserDTO(
        /**
         * id
         */
        val id: String,

        /**
         * email
         */
        val email: String,

        /**
         * todo: 암호화 처리
         */
        val password: String
)

fun User.toDTO() = UserDTO(
        id, email, password
)