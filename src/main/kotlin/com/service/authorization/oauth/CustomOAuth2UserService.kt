package com.service.authorization.oauth

import com.service.authorization.user.UserCreationPayload
import com.service.authorization.user.UserDTO
import com.service.authorization.user.UserService
import com.service.authorization.user.toDTO
import com.service.authorization.userFederatedIdentity.UserFederatedIdentityService
import com.service.authorization.userRole.*
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.OidcIdToken
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
        // 계정 - 소셜 연동된 경우
        val oidcUser = oidcUser(userRequest.idToken)
        if (oidcUser != null) {
            return oidcUser
        }
        // 계정, 소셜이 없는 경우: 계정, 소셜 추가
        // 계정은 있고 소셜이 없는 경우: 계정에 소셜 연동
        return newOidcUser(userRequest.idToken, userRequest.clientRegistration.registrationId)
    }

    fun findUser(subject: String): UserDTO? {
        userService.getById(subject)?.let {
            return it.toDTO()
        }
        val federatedUser = userFederatedIdentityService.getBy(subject) ?: return null
        return userService.getById(federatedUser.userId)?.toDTO() ?: throw Exception("not found user")
    }

    private fun newOidcUser(idToken: OidcIdToken, registrationId: String): OidcUser {
        val userId = getOrSaveUser(idToken.email).id
        val roles = userRoleService.getAllOrSave(userId, UserRoleCreationPayload(UserRoleName.ROLE_USER))
        userFederatedIdentityService.save(idToken.subject, userId, registrationId)
        val newToken = OidcIdToken.withTokenValue(idToken.tokenValue)
                .subject(userId)
                .build()
        return DefaultOidcUser(roles, newToken)
    }

    private fun getOrSaveUser(email: String) = userService.getBy(email = email)
            ?: userService.save(UserCreationPayload(email = email, password = ""))

    private fun oidcUser(idToken: OidcIdToken): OidcUser? {
        val userId = userFederatedIdentityService.getBy(idToken.subject)?.userId ?: return null
        val roles = userRoleService.getAllBy(userId).map(UserRole::toGrantedAuthority)
        val newToken = OidcIdToken.withTokenValue(idToken.tokenValue)
                .subject(userId)
                .build()
        return DefaultOidcUser(roles, newToken)
    }

    fun oidcUserInfo(username: String): Map<String, Any>? {
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