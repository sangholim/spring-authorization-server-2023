package com.service.authorization.userRole

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import java.util.UUID

/**
 * 회원 권한 테이블
 */
@Entity
class UserRole(
        /**
         * id
         */
        @Id
        val id: String,

        /**
         * userId
         */
        val userId: String,

        /**
         * 권한
         */
        val role: String
) {
    companion object {
        /**
         * 회원 권한 객체 생성
         */
        fun of(userId: String, authority: GrantedAuthority) = UserRole(
                id = UUID.randomUUID().toString(),
                userId = userId,
                role = authority.authority
        )
    }
}