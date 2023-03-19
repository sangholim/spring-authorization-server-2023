package com.service.authorization.userFederatedIdentity

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

interface UserFederatedIdentityRepository : JpaRepository<UserFederatedIdentity, String> {

    fun findByUserId(userId: String): List<UserFederatedIdentity>

    @Transactional
    fun deleteAllByUserIdAndIdIn(userId: String, ids: Set<String>)
}