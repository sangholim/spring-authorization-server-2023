package com.service.authorization.config

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.MockkBeans
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain

@MockkBeans(value = [
    MockkBean(value = [SecurityConfig::class], relaxed = true)
])
@EnableWebSecurity
@TestConfiguration
class TestSecurityConfig(
        clientRegistrationRepository: ClientRegistrationRepository,
        customerOAuth2UserService: OAuth2UserService<OidcUserRequest, OidcUser>,
        userNameAndPasswordService: UserDetailsService,
        jwtDecoder: JwtDecoder
) {
    private val securityConfigImpl = SecurityConfig(clientRegistrationRepository, customerOAuth2UserService, userNameAndPasswordService, jwtDecoder)

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Throws(java.lang.Exception::class)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return securityConfigImpl.authorizationServerSecurityFilterChain(http.csrf().disable())
    }


    @Bean
    @Order(2)
    @Throws(java.lang.Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return securityConfigImpl.defaultSecurityFilterChain(http.csrf().disable())
    }
}