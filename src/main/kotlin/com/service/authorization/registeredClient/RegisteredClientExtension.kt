package com.service.authorization.registeredClient

import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient

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
