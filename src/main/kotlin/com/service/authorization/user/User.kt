package com.service.authorization.user

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.security.core.userdetails.UserDetails
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
    companion object {
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

    fun update(payload: UserUpdatePayload): User =
            User(this.id, payload.email, this.password, payload.enabled)
}