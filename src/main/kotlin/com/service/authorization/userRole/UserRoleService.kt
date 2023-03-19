package com.service.authorization.userRole

import com.service.authorization.userRole.UserRole.Companion.userRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service

@Service
class UserRoleService(
        private val userRoleRepository: UserRoleRepository
) {

    fun getAllOrSave(userId: String, payload: UserRoleCreationPayload): List<GrantedAuthority> {
        val roles = getAllBy(userId = userId).map(UserRole::toGrantedAuthority)
        if (roles.isNotEmpty()) {
            return roles
        }

        return listOf(save(userId, payload).toGrantedAuthority())
    }

    fun save(userId: String, payload: UserRoleCreationPayload): UserRole {
        val count = userRoleRepository.findByUserId(userId).count { it.name == payload.name }
        if (count > 0) throw Exception("이미 등록된 권한입니다")
        return userRole {
            this.userId = userId
            this.name = payload.name
        }.run(userRoleRepository::save)
    }

    fun getAllBy(userId: String): List<UserRole> =
            userRoleRepository.findByUserId(userId)

    fun deleteByIds(userId: String, ids: Set<String>) {
        userRoleRepository.deleteAllByUserIdAndIdIn(userId, ids)
    }

}