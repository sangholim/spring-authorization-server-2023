package com.service.authorization.registeredClient

import com.service.authorization.config.SecurityConstants
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import java.util.*

object RegisteredClientFactory {

    fun create(payload: RegisteredClientCreationPayload): RegisteredClient =
            RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId(payload.clientId)
                    .clientSecret("${SecurityConstants.PASSWORD_PREFIX}${payload.clientSecret}")
                    .clientAuthenticationMethod(ClientAuthenticationMethod(payload.clientAuthenticationMethod))
                    .authorizationGrantTypes { types ->
                        types.addAll(payload.authorizationGrantTypes.map { AuthorizationGrantType(it) })
                    }
                    .redirectUris {
                        it.addAll(payload.validRedirectUris)
                    }
                    .scopes {
                        it.addAll(payload.validScopes)
                    }
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                    .build()
}