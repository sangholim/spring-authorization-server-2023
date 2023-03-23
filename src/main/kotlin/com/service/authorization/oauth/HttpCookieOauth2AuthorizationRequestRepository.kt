package com.service.authorization.oauth

import com.service.authorization.util.CookieUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.util.Assert

class HttpCookieOauth2AuthorizationRequestRepository : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private val authorizationRequestName = HttpCookieOauth2AuthorizationRequestRepository::class.java
            .name + ".AUTHORIZATION_REQUEST"
    private val redirectUriName = "redirect_uri"

    private val maxAge = 180

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        Assert.notNull(request, "request cannot be null")
        val stateParameter = getStateParameter(request) ?: return null
        val authorizationRequest = getAuthorizationRequest(request) ?: return null
        if (authorizationRequest.state != stateParameter) return null
        return authorizationRequest
    }

    override fun saveAuthorizationRequest(authorizationRequest: OAuth2AuthorizationRequest?, request: HttpServletRequest,
                                          response: HttpServletResponse) {
        Assert.notNull(request, "request cannot be null")
        Assert.notNull(response, "response cannot be null")
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response)
            return
        }
        val state = authorizationRequest.state
        Assert.hasText(state, "authorizationRequest.state cannot be empty")
        val authorizationRequestSerializer = CookieUtils.serialize(authorizationRequest)
        CookieUtils.addCookie(response, authorizationRequestName, authorizationRequestSerializer, maxAge)
        val redirectUri = request.getParameter(redirectUriName)
        if (!redirectUri.isNullOrBlank()) {
            CookieUtils.addCookie(response, redirectUriName, redirectUri, maxAge)
        }
    }

    override fun removeAuthorizationRequest(request: HttpServletRequest, response: HttpServletResponse): OAuth2AuthorizationRequest? {
        Assert.notNull(response, "response cannot be null")
        val authorizationRequest = loadAuthorizationRequest(request)
        if (authorizationRequest != null) {
            CookieUtils.deleteCookie(request, response, authorizationRequestName)
            CookieUtils.deleteCookie(request, response, redirectUriName)
        }
        return authorizationRequest
    }

    /**
     * Gets the state parameter from the [HttpServletRequest]
     * @param request the request to use
     * @return the state parameter or null if not found
     */
    private fun getStateParameter(request: HttpServletRequest): String? {
        return request.getParameter(OAuth2ParameterNames.STATE)
    }

    private fun getAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        val cookie = CookieUtils.getCookie(request, authorizationRequestName) ?: return null
        return CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest::class.java)
    }
}