package com.service.authorization.userRole

import org.springframework.data.jpa.repository.JpaRepository

interface UserRoleRepository : JpaRepository<UserRole, String> {

    fun findByUserId(userId: String): List<UserRole>
}