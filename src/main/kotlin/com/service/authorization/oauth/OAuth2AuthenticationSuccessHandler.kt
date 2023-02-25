package com.service.authorization.oauth

import com.service.authorization.token.TokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder


@Component
class OAuth2AuthenticationSuccessHandler(
        private val tokenProvider: TokenProvider
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val redirectUrl = determineTargetUrl(request, response, authentication)
        redirectStrategy.sendRedirect(request, response, redirectUrl)
        clearAuthenticationAttributes(request)
    }


    override fun determineTargetUrl(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication): String {
        val targetUrl: String = defaultTargetUrl
        val accessToken = tokenProvider.createToken(authentication)
        val refreshToken = tokenProvider.createToken(authentication)

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString()
    }
}