package com.service.authorization.oauth

import com.service.authorization.user.UserDTO
import com.service.authorization.user.UserService
import com.service.authorization.user.toDTO
import com.service.authorization.userFederatedIdentity.UserFederatedIdentityService
import com.service.authorization.userRole.UserRoleService
import org.springframework.security.core.userdetails.User
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class CustomOAuth2UserService(
        private val userService: UserService,
        private val userRoleService: UserRoleService,
        private val userFederatedIdentityService: UserFederatedIdentityService
) : OAuth2UserService<OidcUserRequest, OidcUser> {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        // 계정 - 소셜이 없는 경우: 계정 소셜  추가
        // 계정은 있고 소셜이 없는 경우: 계정에 소셜 연동
        val subject = userRequest.idToken.subject
        val email = userRequest.idToken.email
        val userDetail = User.withDefaultPasswordEncoder()
                .username(email)
                .password("federated")
                .roles("USER")
                .build()
        val user = userService.getBy(email = email) ?: userService.save(userDetail)
        val userRoles = userRoleService.getAllOrSave(user.id, userDetail.authorities.toList())
        userFederatedIdentityService.getOrSave(subject, user.id, userRequest.clientRegistration.registrationId)
        return DefaultOidcUser(userRoles, userRequest.idToken)
    }

    fun findUser(subject: String): UserDTO? {
        userService.getById(subject)?.let {
            return it.toDTO()
        }
        val federatedUser = userFederatedIdentityService.getBy(subject) ?: return null
        return userService.getById(federatedUser.userId)?.toDTO() ?: throw Exception("not found user")
    }

    fun oidcUser(username: String): Map<String, Any>? {
        val user = findUser(username) ?: return null

        return OidcUserInfo.builder()
                .subject(user.id)
                .preferredUsername(user.id)
                .email(user.email)
                .emailVerified(true)
                .build()
                .claims
    }
}