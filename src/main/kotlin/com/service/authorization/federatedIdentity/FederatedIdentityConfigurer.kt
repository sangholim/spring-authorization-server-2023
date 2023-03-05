package com.service.authorization.federatedIdentity

import com.service.authorization.config.SecurityConstants
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class FederatedIdentityConfigurer(
        private val clientRegistrationRepository: ClientRegistrationRepository,
        private val customerOAuth2UserService: OAuth2UserService<OidcUserRequest, OidcUser>
) : AbstractHttpConfigurer<FederatedIdentityConfigurer, HttpSecurity>() {

    override fun init(http: HttpSecurity) {
        http.exceptionHandling { exceptions ->
            exceptions.authenticationEntryPoint(FederatedIdentityAuthenticationEntryPoint(clientRegistrationRepository))
        }.oauth2Login()
                .defaultSuccessUrl(SecurityConstants.SUCCESS_URL)
                .userInfoEndpoint()
                .oidcUserService(customerOAuth2UserService)
    }
}