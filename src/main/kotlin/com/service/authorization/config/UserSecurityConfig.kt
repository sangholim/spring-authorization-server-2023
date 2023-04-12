package com.service.authorization.config

import com.service.authorization.oauth.CustomOAuth2UserService
import com.service.authorization.user.CustomUserDetailsService
import com.service.authorization.user.UserService
import com.service.authorization.userFederatedIdentity.UserFederatedIdentityService
import com.service.authorization.userRole.UserRoleService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser

@Configuration
class UserSecurityConfig {

    @Bean
    fun customUserDetailsService(userService: UserService, userRoleService: UserRoleService): CustomUserDetailsService =
            CustomUserDetailsService(userService, userRoleService)

    @Bean
    fun customOAuth2UserService(userService: UserService, userRoleService: UserRoleService, userFederatedIdentityService: UserFederatedIdentityService): OAuth2UserService<OidcUserRequest, OidcUser> =
            CustomOAuth2UserService(userService, userRoleService, userFederatedIdentityService)
}