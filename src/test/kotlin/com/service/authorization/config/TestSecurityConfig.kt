package com.service.authorization.config

import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.MockBeans
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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

@MockBeans(value = [
    MockBean(SecurityConfig::class)
])
@EnableWebSecurity
@Configuration
class TestSecurityConfig(
        clientRegistrationRepository: ClientRegistrationRepository,
        customerOAuth2UserService: OAuth2UserService<OidcUserRequest, OidcUser>,
        userNameAndPasswordService: UserDetailsService,
        jwtDecoder: JwtDecoder
) {
    private val securityConfig = SecurityConfig(clientRegistrationRepository, customerOAuth2UserService, userNameAndPasswordService, jwtDecoder)

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Throws(java.lang.Exception::class)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
        return securityConfig.authorizationServerSecurityFilterChain(http)
    }


    @Bean
    @Order(2)
    @Throws(java.lang.Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
        return securityConfig.defaultSecurityFilterChain(http)
    }
}