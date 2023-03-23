package com.service.authorization.federatedIdentity

import com.service.authorization.config.SecurityConstants
import com.service.authorization.oauth.HttpCookieOauth2AuthorizationRequestRepository
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.savedrequest.CookieRequestCache

class FederatedIdentityConfigurer(
        private val clientRegistrationRepository: ClientRegistrationRepository,
        private val customerOAuth2UserService: OAuth2UserService<OidcUserRequest, OidcUser>,
        private val httpCookieOauth2AuthorizationRequestRepository: HttpCookieOauth2AuthorizationRequestRepository
) : AbstractHttpConfigurer<FederatedIdentityConfigurer, HttpSecurity>() {

    override fun init(http: HttpSecurity) {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.requestCache().requestCache(CookieRequestCache())
        http.exceptionHandling { exceptions ->
            exceptions.authenticationEntryPoint(FederatedIdentityAuthenticationEntryPoint(clientRegistrationRepository))
        }.oauth2Login()
                .authorizationEndpoint()
                .authorizationRequestRepository(httpCookieOauth2AuthorizationRequestRepository)
                .and()
                .defaultSuccessUrl(SecurityConstants.SUCCESS_URL)
                .userInfoEndpoint()
                .oidcUserService(customerOAuth2UserService)
    }
}