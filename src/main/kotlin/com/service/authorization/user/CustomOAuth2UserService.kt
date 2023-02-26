package com.service.authorization.user

import com.service.authorization.userRole.UserRoleService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class CustomOAuth2UserService(
        private val userService: UserService,
        private val userRoleService: UserRoleService
) : OAuth2UserService<OidcUserRequest, OidcUser> {

    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        val email = userRequest.idToken.email
        val userDetail = User.withDefaultPasswordEncoder()
                .username(email)
                .password("federated")
                .roles("USER")
                .build()
        val user = userService.getBy(email = email) ?: userService.save(userDetail)
        val userRoles = userRoleService.getAllBy(userId = user.id)
                .map { SimpleGrantedAuthority(it.role) }.let { it ->
                    if (!it.containsAll(userDetail.authorities)) {
                        return@let userRoleService.saveAll(user.id, userDetail.authorities.toList()).map { SimpleGrantedAuthority(it.role) }
                    }
                    it
                }

        return DefaultOidcUser(userRoles, userRequest.idToken)
    }
}