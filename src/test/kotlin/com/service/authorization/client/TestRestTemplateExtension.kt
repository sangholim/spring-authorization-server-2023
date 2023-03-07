package com.service.authorization.client

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
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