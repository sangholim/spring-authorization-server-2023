package com.service.authorization

import com.service.authorization.registeredClient.CustomRegisteredClientRepository
import com.service.authorization.user.UserService
import com.service.authorization.userRole.UserRoleService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.stereotype.Component
import java.util.*

@Component
class MigrationRunner(
        private val userService: UserService,
        private val userRoleService: UserRoleService,
        private val registeredClientRepository: CustomRegisteredClientRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val userDetails: UserDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build()
        if (userService.getBy(userDetails.username) != null) {
            return
        }
        val user = userService.save(userDetails)
        userRoleService.saveAll(user.id, userDetails.authorities.toList())
        val registeredClients = listOf(
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("public-client")
                        .clientSecret("{noop}public-secret")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri("https://oauthdebugger.com/debug")
                        .scope(OidcScopes.OPENID)
                        .scope(OidcScopes.PROFILE)
                        .scope("message.read")
                        .scope("message.write")
                        .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                        .build(),
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("internal-client")
                        .clientSecret("{noop}internal-client-secret")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .redirectUri("https://oauthdebugger.com/debug")
                        .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                        .build()
                )
        registeredClientRepository.deleteAll()
        registeredClients.forEach {
            registeredClientRepository.save(it)
        }
    }
}