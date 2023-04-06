package com.service.authorization.oauth

import com.service.authorization.userRole.UserRoleService
import com.service.authorization.userRole.excludeAdmin
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.server.authorization.OAuth2TokenIntrospection
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken
import org.springframework.security.oauth2.server.authorization.http.converter.OAuth2TokenIntrospectionHttpMessageConverter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

/**
 * token-introspect 검증 끝난후 처리 로직
 */
class OAuth2TokenIntrospectionAuthenticationSuccessHandler(
        private val userRoleService: UserRoleService
) : AuthenticationSuccessHandler {
    private val tokenIntrospectionHttpResponseConverter = OAuth2TokenIntrospectionHttpMessageConverter()
    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val tokenIntrospectionAuthentication = authentication as OAuth2TokenIntrospectionAuthenticationToken
        val clientPrincipal = tokenIntrospectionAuthentication.principal as OAuth2ClientAuthenticationToken
        val authorizationGrantTypes = clientPrincipal.registeredClient?.authorizationGrantTypes
        val tokenIntrospection = tokenIntrospectionAuthentication.tokenClaims
        val claims = tokenIntrospectionAuthentication.tokenClaims.claims.toMutableMap()
        if (tokenIntrospection.isActive) {
            claims.putAll(additionalClaims(tokenIntrospection.subject, authorizationGrantTypes))
        }
        val token = OAuth2TokenIntrospection.withClaims(claims).build()
        val httpResponse = ServletServerHttpResponse(response)
        this.tokenIntrospectionHttpResponseConverter.write(token, null, httpResponse)
    }

    /**
     * 회원 번호로 조회한 정보를 claims 으로 추가하는 로직
     * 회원 번호가 있는 경우
     * register-client authorizationGrantTypes 이 client_credentials 을 포함한 경우에만 허용한다
     */
    private fun additionalClaims(userId: String?, authorizationGrantTypes: Set<AuthorizationGrantType>?): Map<String, Any> {
        val claims = mutableMapOf<String, Any>()
        if (userId == null || authorizationGrantTypes == null) return emptyMap()
        if (authorizationGrantTypes.count { it == AuthorizationGrantType.CLIENT_CREDENTIALS } == 0) return emptyMap()
        userRoleService.getAllBy(userId).excludeAdmin().ifEmpty { null }?.map { it.name.toString() }?.run {
            claims.putIfAbsent("roles", this)
        }
        return claims
    }
}