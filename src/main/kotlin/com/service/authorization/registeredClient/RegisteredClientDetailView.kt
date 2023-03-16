package com.service.authorization.registeredClient

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import java.time.Instant

data class RegisteredClientDetailView(
        val id: String,

        val clientId: String,

        val clientIdIssuedAt: Instant?,

        val clientSecret: String?,

        val clientSecretExpiresAt: Instant?,

        val clientName: String,

        val clientAuthenticationMethods: List<String>,

        val authorizationGrantTypes: List<String>,

        val redirectUris: Set<String>,

        val scopes: Set<String>,
)

fun RegisteredClient.toDetailView() = RegisteredClientDetailView(
        id = id,
        clientId = clientId,
        clientIdIssuedAt = clientIdIssuedAt,
        clientSecret = clientSecret,
        clientSecretExpiresAt = clientSecretExpiresAt,
        clientName = clientName,
        clientAuthenticationMethods = clientAuthenticationMethods.map { it.value },
        authorizationGrantTypes = authorizationGrantTypes.map { it.value },
        redirectUris = redirectUris,
        scopes = scopes
)