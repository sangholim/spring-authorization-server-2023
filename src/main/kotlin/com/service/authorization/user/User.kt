package com.service.authorization.user

import com.service.authorization.userFederatedIdentity.UserFederatedIdentity
import com.service.authorization.userRole.UserRole
import com.service.authorization.util.RandomUtil
import jakarta.persistence.*
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
        var id: String,

        /**
         * email
         */
        var email: String,

        /**
         * todo: 암호화 처리
         */
        var password: String,

        /**
         * 활성화 여부
         */
        var enabled: Boolean
) {
    /**
     * 회원 권한
     */
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private val _roles: MutableSet<UserRole> = mutableSetOf()
    val roles: MutableSet<UserRole> get() = _roles

    /**
     * 소셜 연동
     */
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private val _federatedIdentities: MutableSet<UserFederatedIdentity> = mutableSetOf()
    val federatedIdentities: MutableSet<UserFederatedIdentity> get() = _federatedIdentities

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

        fun from(user: User): Builder = Builder().apply {
            this.id = user.id
            this.email = user.email
            this.password = user.password
            this.enabled = user.enabled
        }
    }

    class Builder {
        var id: String? = UUID.randomUUID().toString()
        var email: String = ""
        var password: String = ""
        var enabled: Boolean = false

        fun build(): User {
            if (password.isEmpty()) {
                password = encodePassword(RandomUtil.generatePassword())
            }
            return User(this)
        }

        fun password(password: String): Builder {
            this.password = encodePassword(password)
            return this
        }

        private fun encodePassword(password: String): String {
            val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
            return encoder.encode(password)
        }
    }

    fun update(payload: UserUpdatePayload) =
            User(this.id, payload.email, this.password, payload.enabled)
}