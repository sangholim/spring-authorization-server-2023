package com.service.authorization.userRole

import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service

@Service
class UserRoleService(
        private val userRoleRepository: UserRoleRepository
) {

    fun saveAll(userId: String, authorities: List<GrantedAuthority>): List<UserRole> =
            authorities.map { UserRole.of(userId, it) }.run {
                userRoleRepository.saveAll(this)
            }

    fun getAllBy(userId: String): List<UserRole> =
            userRoleRepository.findByUserId(userId)
}