package com.service.authorization.config

import com.service.authorization.federatedIdentity.FederatedIdentityConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.util.*


@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Throws(java.lang.Exception::class)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
                .oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
        http // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling { exceptions: ExceptionHandlingConfigurer<HttpSecurity?> ->
                    exceptions
                            .authenticationEntryPoint(
                                    LoginUrlAuthenticationEntryPoint("/login"))
                } // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer { it.jwt() }
        return http.build()
    }


    @Bean
    @Order(2)
    @Throws(java.lang.Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .authorizeHttpRequests { authorize ->
                    authorize
                            .requestMatchers("/assets/**", "/webjars/**", "/login").permitAll()
                            .anyRequest().authenticated()
                } // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(Customizer.withDefaults())
        http.apply(FederatedIdentityConfigurer())
        return http.build()
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:9090")
                .authorizationEndpoint("/oauth2/v1/authorize")
                .tokenEndpoint("/oauth2/v1/token")
                .tokenIntrospectionEndpoint("/oauth2/v1/introspect")
                .tokenRevocationEndpoint("/oauth2/v1/revoke")
                .jwkSetEndpoint("/oauth2/v1/jwks")
                .oidcUserInfoEndpoint("/connect/v1/userinfo")
                .oidcClientRegistrationEndpoint("/connect/v1/register")
                .build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val userDetails: UserDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build()
        return InMemoryUserDetailsManager(userDetails)
    }
}
