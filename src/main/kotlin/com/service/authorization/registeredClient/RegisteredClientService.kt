package com.service.authorization.registeredClient

import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.stereotype.Service
import java.util.*


@Service
class RegisteredClientService(
        private val registeredClientRepository: CustomRegisteredClientRepository
) {

    fun getAllBy() = registeredClientRepository.findBy()

    fun getBy(id: String) = registeredClientRepository.findById(id) ?: throw Exception("존재하지 않는 client 입니다")

    fun save(payload: RegisteredClientCreationPayload) {
        if (registeredClientRepository.findByClientId(payload.clientId) != null) throw Exception("등록된 client-id 입니다")
        val registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(payload.clientId)
                .clientSecret(payload.clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod(payload.clientAuthenticationMethod))
                .authorizationGrantTypes { types ->
                    types.addAll(payload.authorizationGrantTypes.map { AuthorizationGrantType(it) })
                }
                .redirectUris {
                    it.addAll(payload.redirectUris)
                }
                .scopes {
                    it.addAll(payload.scopes)
                }
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build()
        registeredClientRepository.save(registeredClient)
    }
}