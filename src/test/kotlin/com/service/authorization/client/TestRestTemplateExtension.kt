package com.service.authorization.client

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

fun TestRestTemplate.login(port: Int, username: String = "user", password: String = "password"): ResponseEntity<String> {
    val url = "http://localhost:$port"
    val headers = HttpHeaders().apply {
        this["content-type"] = "application/x-www-form-urlencoded"
    }
    val body = LinkedMultiValueMap<String, Any>().apply {
        this["username"] = username
        this["password"] = password
    }
    val entity = HttpEntity(body, headers)
    return exchange("$url/login", HttpMethod.POST, entity, String::class.java)
}

fun TestRestTemplate.oauth2Authorize(sessionId: String, clientId: String, authorizeUrl: String, redirectUrl: String?): ResponseEntity<String> {
    val headers = HttpHeaders().apply {
        this[HttpHeaders.COOKIE] = "SESSION=$sessionId"
    }

    val url = UriComponentsBuilder.fromHttpUrl(authorizeUrl)
            .queryParam("response_type", "code")
            .queryParam("scope", "openid")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUrl)
            .toUriString()
    val entity = HttpEntity(null, headers)
    return exchange(url, HttpMethod.GET, entity)
}


fun oauth2Token(clientId: String, clientSecret: String?, tokenUrl: String, code: String?, redirectUrl: String?): OAuth2AccessTokenResponse? {
    val restTemplate = RestTemplate(
            listOf(FormHttpMessageConverter(), OAuth2AccessTokenResponseHttpMessageConverter()))
    restTemplate.errorHandler = OAuth2ErrorResponseErrorHandler()
    val headers = HttpHeaders().apply {
        this.setBasicAuth(clientId, clientSecret ?: "")
        this["content-type"] = "application/x-www-form-urlencoded"
    }
    val body = LinkedMultiValueMap<String, Any>().apply {
        this["grant_type"] = AuthorizationGrantType.AUTHORIZATION_CODE.value
        this["code"] = code
        this["redirect_uri"] = redirectUrl
    }
    val entity = HttpEntity(body, headers)
    return restTemplate.postForObject(tokenUrl, entity, OAuth2AccessTokenResponse::class.java)
}