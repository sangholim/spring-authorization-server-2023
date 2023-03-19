package com.service.authorization.userRole

import com.service.authorization.userRole.UserRole.Companion.userRole
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

    fun save(userId: String, payload: UserRoleCreationPayload): UserRole {
        val count = userRoleRepository.findByUserId(userId).count { it.name == payload.name.name }
        if (count > 0) throw Exception("이미 등록된 권한입니다")
        return userRole {
            this.userId = userId
            this.name = payload.name.name
        }.run(userRoleRepository::save)
    }

    fun getAllBy(userId: String): List<UserRole> =
            userRoleRepository.findByUserId(userId)
}