package com.service.authorization.oauth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.util.StringUtils
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.web.util.UriUtils
import java.nio.charset.StandardCharsets

class Oauth2AuthorizationAuthenticationSuccessHandler: AuthenticationSuccessHandler {

    private val redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val authorizationCodeRequestAuthentication = authentication as OAuth2AuthorizationCodeRequestAuthenticationToken
        val uriBuilder = UriComponentsBuilder
                .fromUriString(authorizationCodeRequestAuthentication.redirectUri!!)
                .queryParam(OAuth2ParameterNames.CODE, authorizationCodeRequestAuthentication.authorizationCode!!.tokenValue)
        if (StringUtils.hasText(authorizationCodeRequestAuthentication.state)) {
            uriBuilder.queryParam(
                    OAuth2ParameterNames.STATE,
                    UriUtils.encode(authorizationCodeRequestAuthentication.state!!, StandardCharsets.UTF_8))
        }
        val redirectUri = uriBuilder.build(true).toUriString() // build(true) -> Components are explicitly encoded
        // 성공이 끝난후 세션 제거
        request.getSession(false)?.invalidate()
        this.redirectStrategy.sendRedirect(request, response, redirectUri)
    }
}