package com.service.authorization.federatedIdentity

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint

class FederatedIdentityConfigurer(
        private val customerOAuth2UserService: OAuth2UserService<OidcUserRequest, OidcUser>
) : AbstractHttpConfigurer<FederatedIdentityConfigurer, HttpSecurity>() {

    override fun init(http: HttpSecurity) {
        http.exceptionHandling { exceptions ->
            exceptions.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint(""))
        }.oauth2Login()
                .defaultSuccessUrl("/console/frame")
                .userInfoEndpoint()
                .oidcUserService(customerOAuth2UserService)
    }
}