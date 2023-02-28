package com.service.authorization.userRole

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class UserRoleService(
        private val userRoleRepository: UserRoleRepository
) {

    fun getAllOrSave(userId: String, authorities: List<GrantedAuthority>): List<GrantedAuthority> {
        val roles = getAllBy(userId = userId).map { SimpleGrantedAuthority(it.role) }
        if (roles.containsAll(authorities)) {
            return roles
        }
        return saveAll(userId, authorities).map { SimpleGrantedAuthority(it.role) }
    }

    fun saveAll(userId: String, authorities: List<GrantedAuthority>): List<UserRole> =
            authorities.map { UserRole.of(userId, it) }.run {
                userRoleRepository.saveAll(this)
            }

    fun getAllBy(userId: String): List<UserRole> =
            userRoleRepository.findByUserId(userId)
}