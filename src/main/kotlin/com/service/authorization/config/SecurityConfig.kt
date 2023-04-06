package com.service.authorization.config

import com.service.authorization.oauth.OAuth2TokenIntrospectionAuthenticationSuccessHandler
import com.service.authorization.oauth.Oauth2AuthorizationAuthenticationSuccessHandler
import com.service.authorization.userRole.UserRoleName
import com.service.authorization.userRole.UserRoleService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.util.*

@EnableWebSecurity
@Configuration
class SecurityConfig(
        private val oauth2Config: Oauth2Config,
        private val jwtDecoder: JwtDecoder,
        private val userRoleService: UserRoleService
) {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Throws(java.lang.Exception::class)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
                //.oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
                .oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0
                .authorizationEndpoint { endpoint ->
                    endpoint.authorizationResponseHandler(Oauth2AuthorizationAuthenticationSuccessHandler())
                }.tokenIntrospectionEndpoint { endpoint ->
                    endpoint.introspectionResponseHandler(OAuth2TokenIntrospectionAuthenticationSuccessHandler(userRoleService))
                }
        http.exceptionHandling { exceptions ->
            exceptions
                    .authenticationEntryPoint(
                            LoginUrlAuthenticationEntryPoint("/"))
        }
        http.oauth2ResourceServer { it.jwt().decoder(jwtDecoder) }
        return http.build()
    }


    @Bean
    @Order(2)
    @Throws(java.lang.Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity, userNameAndPasswordService: UserDetailsService): SecurityFilterChain {
        http.authorizeHttpRequests { authorize ->
            authorize
                    .requestMatchers("/assets/**", "/webjars/**", "/", "", "/login/**", "/login").permitAll()
                    .requestMatchers("/console/**").hasAnyAuthority(UserRoleName.ROLE_ADMIN.name)
                    .anyRequest().authenticated()
        }
                .formLogin()
                .defaultSuccessUrl(SecurityConstants.SUCCESS_URL)
                .and()
                .userDetailsService(userNameAndPasswordService)
                .oauth2ResourceServer { it.jwt().decoder(jwtDecoder) }
        http.apply(oauth2Config.federatedIdentityConfig)
        return http.build()
    }
}
