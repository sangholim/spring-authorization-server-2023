package com.service.authorization.config

import com.service.authorization.federatedIdentity.FederatedIdentityConfigurer
import com.service.authorization.user.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.util.*


@EnableWebSecurity
@Configuration
class SecurityConfig(
        private val clientRegistrationRepository: ClientRegistrationRepository,
        private val customerOAuth2UserService: OAuth2UserService<OidcUserRequest, OidcUser>,
        private val userNameAndPasswordService: UserDetailsService
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
                            LoginUrlAuthenticationEntryPoint("/login"))
        }
        http.apply(FederatedIdentityConfigurer(clientRegistrationRepository, customerOAuth2UserService))
        http.oauth2ResourceServer { it.jwt() }
        return http.build()
    }


    @Bean
    @Order(2)
    @Throws(java.lang.Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { authorize ->
            authorize
                    .requestMatchers("/assets/**", "/webjars/**", "/", "", "/login/**").permitAll()
                    .anyRequest().authenticated()
        }
                .formLogin()
                .defaultSuccessUrl("/console/frame")
                .and()
                .userDetailsService(userNameAndPasswordService)
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
    fun tokenCustomizer(userInfoService: CustomOAuth2UserService): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer { context: JwtEncodingContext ->
            if (OidcParameterNames.ID_TOKEN == context.tokenType.value) {
                println("test: ${context.getPrincipal<Authentication>()}")
                //val userInfo: OidcUserInfo = userInfoService.loadUser(
                        //context.getPrincipal<Authentication>().name)
                //context.claims.claims { claims: MutableMap<String?, Any?> -> claims.putAll(userInfo.claims) }
            }
        }
    }
}
