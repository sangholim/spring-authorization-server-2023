package com.service.authorization.userFederatedIdentity

import jakarta.persistence.Entity
import jakarta.persistence.Id

/**
 * 회원 연동 테이블
 */
@Entity
class UserFederatedIdentity(
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
         * 제공자 등록 id
         */
        val registrationId: String
) {
    companion object {
        fun of(id: String, userId: String, registrationId: String) =
                UserFederatedIdentity(id, userId, registrationId)
    }
}