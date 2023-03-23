package com.service.authorization.federatedIdentity

import com.service.authorization.config.SecurityConstants
import com.service.authorization.oauth.HttpCookieOauth2AuthorizationRequestRepository
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class FederatedIdentityConfigurer(
        private val customerOAuth2UserService: OAuth2UserService<OidcUserRequest, OidcUser>,
        private val httpCookieOauth2AuthorizationRequestRepository: HttpCookieOauth2AuthorizationRequestRepository
) : AbstractHttpConfigurer<FederatedIdentityConfigurer, HttpSecurity>() {

    override fun init(http: HttpSecurity) {
            http
                .oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestRepository(httpCookieOauth2AuthorizationRequestRepository)
                .and()
                .defaultSuccessUrl(SecurityConstants.SUCCESS_URL)
                .userInfoEndpoint()
                .oidcUserService(customerOAuth2UserService)
    }
}