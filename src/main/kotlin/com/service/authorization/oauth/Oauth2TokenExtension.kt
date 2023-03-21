package com.service.authorization.oauth

import org.springframework.security.oauth2.core.AbstractOAuth2Token
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import java.time.LocalDateTime
import java.time.ZoneId

fun OAuth2Authorization.Token<out AbstractOAuth2Token>?.convertIssuedAt(): LocalDateTime? {
    if (this == null) return null
    if (this.token.issuedAt == null) return null

    return LocalDateTime.ofInstant(this.token.issuedAt, ZoneId.systemDefault())
}


fun OAuth2Authorization.Token<out AbstractOAuth2Token>?.convertExpiresAt(): LocalDateTime? {
    if (this == null) return null
    if (this.token.expiresAt == null) return null
    return LocalDateTime.ofInstant(this.token.expiresAt, ZoneId.systemDefault())
}