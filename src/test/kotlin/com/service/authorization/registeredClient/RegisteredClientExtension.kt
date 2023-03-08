package com.service.authorization.registeredClient

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient

fun RegisteredClient.parseClientSecret(): String {
    val clientSecret = this.clientSecret ?: return ""
    val separator = clientSecret.indexOf("}").plus(1)
    return clientSecret.substring(separator)
}