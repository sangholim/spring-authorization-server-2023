package com.service.authorization.user

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import java.util.UUID

/**
 * 회원 테이블
 */
@Entity(name = "user_")
class User(
        /**
         * id
         */
        @Id
        val id: String,

        /**
         * email
         */
        val email: String,

        /**
         * todo: 암호화 처리
         */
        val password: String,

        /**
         * 활성화 여부
         */
        val enabled: Boolean
) {

    private constructor(builder: Builder) : this(builder.id!!, builder.email, builder.password, builder.enabled)

    companion object {
        inline fun user(block: Builder.() -> Unit) = Builder().apply(block).build()

        /**
         * 회원 객체 생성
         */
        fun of(userDetail: UserDetails): User =
                User(
                        id = UUID.randomUUID().toString(),
                        email = userDetail.username,
                        password = userDetail.password,
                        enabled = userDetail.isEnabled
                )
    }


    class Builder {
        var id: String? = UUID.randomUUID().toString()
        var email: String = ""
        var password: String = ""
        var enabled: Boolean = false

        fun build(): User {
            password = encodePassword()
            return User(this)
        }

        private fun encodePassword(): String {
            val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
            return encoder.encode(password)
        }
    }

    fun update(payload: UserUpdatePayload): User =
            User(this.id, payload.email, this.password, payload.enabled)
}