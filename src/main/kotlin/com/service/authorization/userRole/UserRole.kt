package com.service.authorization.userRole

import jakarta.persistence.*
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
        @Enumerated(EnumType.STRING)
        val name: UserRoleType
) {
    private constructor(builder: Builder) : this(builder.id!!, builder.userId, builder.name!!)

    companion object {

        inline fun userRole(block: Builder.() -> Unit) = Builder().apply(block).build()
    }


    class Builder {
        var id: String? = UUID.randomUUID().toString()
        var userId: String = ""
        var name: UserRoleType? = null

        fun build(): UserRole {
            return UserRole(this)
        }
    }
}