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
         * 권한
         */
        val roles: List<String>? = emptyList()
)

fun User.toDTO(roles: List<String>? = null) = UserDTO(
        id, email, roles
)