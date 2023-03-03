package com.service.authorization.user

import com.service.authorization.userRole.UserRoleService
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class UserNameAndPasswordService(
        private val userService: UserService,
        private val userRoleService: UserRoleService
): UserDetailsService {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.getBy(username) ?: throw Exception("없는 회원 입니다")
        val roles = userRoleService.getAllBy(user.id).map { SimpleGrantedAuthority(it.role) }
        logger.debug("authenticated user: ${user.email}")
        return User.builder().username(user.id).password(user.password).authorities(roles).build()
    }
}