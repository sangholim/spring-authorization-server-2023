package com.service.authorization.config

import com.nimbusds.jose.jwk.JWKSelector
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import com.service.authorization.jose.Jwks
import com.service.authorization.oauth.CustomOAuth2UserService
import com.service.authorization.token.TokenCustomizer
import com.service.authorization.token.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer

@Configuration
class TokenConfig {

    private val rsaKey = Jwks.generateRsa()

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val jwkSet = JWKSet(rsaKey)
        return JWKSource { jwkSelector: JWKSelector, _: SecurityContext? -> jwkSelector.select(jwkSet) }
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource())
    }

    @Bean
    fun jwtEncoder(): JwtEncoder {
        return NimbusJwtEncoder(jwkSource())
    }

    @Bean
    fun tokenProvider() = TokenProvider(rsaKey, jwtEncoder())

    /**
     * 토큰 클레임 커스텀 소스
     */
    @Bean
    fun tokenCustomizer(customerOAuth2UserService: CustomOAuth2UserService):
            OAuth2TokenCustomizer<JwtEncodingContext> {
        return TokenCustomizer(customerOAuth2UserService)
    }
}