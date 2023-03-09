package com.service.authorization.registeredClient

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient

data class RegisteredClientView(
        val id: String,
        val clientId: String,
        val clientSecret: String?,
        val clientName: String,
        val clientAuthenticationMethods: String?,
        val authorizationGrantTypes: String?,
        val redirectUris: String,
        val scopes: String
)

fun RegisteredClient.toView() = RegisteredClientView(
        id = id,
        clientId = clientId,
        clientSecret = clientSecret,
        clientName = clientName,
        clientAuthenticationMethods = clientAuthenticationMethods.joinToString(separator = ",") { it.value },
        authorizationGrantTypes = authorizationGrantTypes.joinToString(separator = ",") { it.value },
        redirectUris = redirectUris.joinToString(separator = ","),
        scopes = scopes.joinToString(separator = ",")
)