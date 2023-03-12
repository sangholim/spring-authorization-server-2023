package com.service.authorization.token

import com.service.authorization.oauth.CustomOAuth2UserService
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer

class TokenCustomizer(
        private val customOAuth2UserService: CustomOAuth2UserService
) : OAuth2TokenCustomizer<JwtEncodingContext> {
    override fun customize(context: JwtEncodingContext) {
        val subject = context.getPrincipal<Authentication>().name
        val type = context.tokenType
        if (OAuth2TokenType.ACCESS_TOKEN.equals(type)) {
            context.claims.claims { claim ->
                customOAuth2UserService.findUser(subject)?.run {
                    claim["sub"] = this.id
                }
            }
        }

        if (OidcParameterNames.ID_TOKEN == type.value) {
            val userInfo = OidcUserInfo(customOAuth2UserService.oidcUser(subject))
            context.claims.claims { claim ->
                claim.putAll(userInfo.claims)
            }
        }
    }
}