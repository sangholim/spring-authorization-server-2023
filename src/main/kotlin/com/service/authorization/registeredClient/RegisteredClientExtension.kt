package com.service.authorization.registeredClient

import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import java.time.Duration

fun RegisteredClient.update(payload: RegisteredClientUpdatePayload): RegisteredClient =
        RegisteredClient
                .from(this)
                .clientAuthenticationMethods {
                    it.clear()
                    it.add(ClientAuthenticationMethod(payload.clientAuthenticationMethod))
                }
                .authorizationGrantTypes { types ->
                    types.clear()
                    types.addAll(payload.authorizationGrantTypes.map { AuthorizationGrantType(it) })
                }
                .redirectUris {
                    it.clear()
                    it.addAll(payload.validRedirectUris)
                }
                .scopes {
                    it.clear()
                    it.addAll(payload.validScopes)
                }.build()

fun RegisteredClient.updateToken(payload: RegisteredClientTokenUpdatePayload): RegisteredClient {
    val tokenSettings = TokenSettings.builder()
            .authorizationCodeTimeToLive(Duration.ofMinutes(payload.authorizationCodeTimeToLive))
            .accessTokenTimeToLive(Duration.ofMinutes(payload.accessTokenTimeToLive))
            .refreshTokenTimeToLive(Duration.ofMinutes(payload.refreshTokenTimeToLive))
            .build()
    return RegisteredClient
            .from(this)
            .tokenSettings(tokenSettings)
            .build()
}