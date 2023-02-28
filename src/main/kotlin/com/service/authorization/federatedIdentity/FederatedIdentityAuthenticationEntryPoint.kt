package com.service.authorization.federatedIdentity

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.web.util.UriComponentsBuilder


class FederatedIdentityAuthenticationEntryPoint(
        private val clientRegistrationRepository: ClientRegistrationRepository
) : AuthenticationEntryPoint {
    private val redirectStrategy = DefaultRedirectStrategy()
    private val authorizationRequestUri = OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/{registrationId}"
    private val delegate = LoginUrlAuthenticationEntryPoint("")

    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        val redirectUrl = redirectUrl(request)
        if (redirectUrl == null) {
            delegate.commence(request, response, authException)
            return
        }
        redirectStrategy.sendRedirect(request, response, redirectUrl)
    }

    private fun redirectUrl(request: HttpServletRequest): String? {
        val idp = request.getParameter("idp") ?: return null
        val clientRegistration: ClientRegistration = clientRegistrationRepository.findByRegistrationId(idp)
                ?: return null
        return UriComponentsBuilder.fromHttpRequest(ServletServerHttpRequest(request))
                .replaceQuery(null)
                .replacePath(authorizationRequestUri)
                .buildAndExpand(clientRegistration.registrationId)
                .toUriString()

    }
}