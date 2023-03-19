package com.service.authorization.userRole

import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service

@Service
class UserRoleService(
        private val userRoleRepository: UserRoleRepository
) {

    fun getAllOrSave(userId: String, authorities: List<GrantedAuthority>): List<GrantedAuthority> {
        val roles = getAllBy(userId = userId).map(UserRole::toGrantedAuthority)
        if (roles.containsAll(authorities)) {
            return roles
        }
        return saveAll(userId, authorities).map(UserRole::toGrantedAuthority)
    }

    fun saveAll(userId: String, authorities: List<GrantedAuthority>): List<UserRole> =
            authorities.map { UserRole.of(userId, it) }.run {
                userRoleRepository.saveAll(this)
            }

    fun getAllBy(userId: String): List<UserRole> =
            userRoleRepository.findByUserId(userId)
}