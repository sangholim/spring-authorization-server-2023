package com.service.authorization.config

import com.service.authorization.federatedIdentity.FederatedIdentityConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.util.*

@EnableWebSecurity
@Configuration
class SecurityConfig(
        private val clientRegistrationRepository: ClientRegistrationRepository,
        private val customerOAuth2UserService: OAuth2UserService<OidcUserRequest, OidcUser>,
        private val userNameAndPasswordService: UserDetailsService,
        private val jwtDecoder: JwtDecoder
) {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Throws(java.lang.Exception::class)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
                .oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
        http.exceptionHandling { exceptions ->
            exceptions
                    .authenticationEntryPoint(
                            LoginUrlAuthenticationEntryPoint("/"))
        }
        http.apply(FederatedIdentityConfigurer(clientRegistrationRepository, customerOAuth2UserService))
        http.oauth2ResourceServer { it.jwt().decoder(jwtDecoder) }
        return http.build()
    }


    @Bean
    @Order(2)
    @Throws(java.lang.Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { authorize ->
            authorize
                    .requestMatchers("/assets/**", "/webjars/**", "/", "", "/login/**", "/login").permitAll()
                    .anyRequest().authenticated()
        }
                .formLogin()
                .defaultSuccessUrl(SecurityConstants.SUCCESS_URL)
                .and()
                .userDetailsService(userNameAndPasswordService)
                .oauth2ResourceServer { it.jwt().decoder(jwtDecoder) }
        http.apply(FederatedIdentityConfigurer(clientRegistrationRepository, customerOAuth2UserService))
        return http.build()
    }
}
