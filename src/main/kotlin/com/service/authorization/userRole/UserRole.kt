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
         * 권한 이름
         */
        val name: String
) {
    private constructor(builder: Builder) : this(builder.id!!, builder.userId, builder.name)

    companion object {

        inline fun userRole(block: Builder.() -> Unit) = Builder().apply(block).build()

        /**
         * 회원 권한 객체 생성
         */
        fun of(userId: String, authority: GrantedAuthority) = UserRole(
                id = UUID.randomUUID().toString(),
                userId = userId,
                name = authority.authority
        )
    }


    class Builder {
        var id: String? = UUID.randomUUID().toString()
        var userId: String = ""
        var name: String = ""

        fun build(): UserRole {
            return UserRole(this)
        }
    }
}