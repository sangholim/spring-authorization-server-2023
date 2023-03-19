package com.service.authorization.userRole

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

interface UserRoleRepository : JpaRepository<UserRole, String> {

    fun findByUserId(userId: String): List<UserRole>

    @Transactional
    fun deleteAllByUserIdAndIdIn(userId: String, ids: Set<String>)
}