package com.service.authorization.oauth

import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import java.time.LocalDateTime

data class OAuth2AuthorizationView(
        val id: String,
        val registeredClientId: String,
        val principalName: String,
        val authorizationGrantTypes: String,
        val authorizedScopes: String,
        val authorizationCodeIssuedAt: LocalDateTime?,
        val authorizationCodeExpiresAt: LocalDateTime?,
        val accessTokenIssuedAt: LocalDateTime?,
        val accessTokenExpiresAt: LocalDateTime?,
        val accessTokenType: String?,
        val accessTokenScopes: String?,
        val oidcIdTokenIssuedAt: LocalDateTime?,
        val oidcIdTokenExpiresAt: LocalDateTime?,
        val refreshTokenIssuedAt: LocalDateTime?,
        val refreshTokenExpiresAt: LocalDateTime?
)

fun OAuth2Authorization.toView(): OAuth2AuthorizationView {
    val authorizationCode = this.getToken(OAuth2AuthorizationCode::class.java)
    val accessToken = this.getToken(OAuth2AccessToken::class.java)
    val oidcToken = this.getToken(OidcIdToken::class.java)
    val refreshToken = this.getToken(OAuth2RefreshToken::class.java)
    return OAuth2AuthorizationView(
            id = this.id,
            registeredClientId = this.registeredClientId,
            principalName = this.principalName,
            authorizationGrantTypes = this.authorizationGrantType.value,
            authorizedScopes = this.authorizedScopes.joinToString(separator = ","),
            authorizationCodeIssuedAt = authorizationCode.convertIssuedAt(),
            authorizationCodeExpiresAt = authorizationCode.convertExpiresAt(),
            accessTokenIssuedAt = accessToken.convertIssuedAt(),
            accessTokenExpiresAt =  accessToken.convertExpiresAt(),
            accessTokenType = accessToken?.token?.tokenType?.value,
            accessTokenScopes = accessToken?.token?.scopes?.joinToString(separator = ","),
            oidcIdTokenIssuedAt = oidcToken.convertIssuedAt(),
            oidcIdTokenExpiresAt = oidcToken.convertExpiresAt(),
            refreshTokenIssuedAt = refreshToken.convertIssuedAt(),
            refreshTokenExpiresAt = refreshToken.convertExpiresAt()
    )
}