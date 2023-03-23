package com.service.authorization.config

import com.service.authorization.federatedIdentity.FederatedIdentityConfigurer
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser


@Configuration
class Oauth2Config(
        private val clientRegistrationRepository: ClientRegistrationRepository,
        private val customerOAuth2UserService: OAuth2UserService<OidcUserRequest, OidcUser>
) {

    val federatedIdentityConfig: FederatedIdentityConfigurer
        get() = FederatedIdentityConfigurer(clientRegistrationRepository, customerOAuth2UserService)

}