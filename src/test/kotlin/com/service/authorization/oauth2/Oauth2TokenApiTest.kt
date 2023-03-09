package com.service.authorization.oauth2

import com.service.authorization.client.login
import com.service.authorization.client.oauth2Authorize
import com.service.authorization.client.oauth2Token
import com.service.authorization.config.TestSecurityConfig
import com.service.authorization.htpHeader.encodedSessionId
import com.service.authorization.registeredClient.CustomRegisteredClientRepository
import com.service.authorization.registeredClient.RegisteredClientConstants
import com.service.authorization.registeredClient.parseClientSecret
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames.ID_TOKEN
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = [TestSecurityConfig::class]
)
class Oauth2TokenApiTest(
        @LocalServerPort
        private val port: Int,
        private val registeredClientRepository: CustomRegisteredClientRepository,
        private val authorizationServerSettings: AuthorizationServerSettings,
) : BehaviorSpec({
    beforeSpec {
        registeredClientRepository.deleteAll()
        registeredClientRepository.save(RegisteredClientConstants.defaultClient)
    }

    Given("oauth2 토큰 발급 (OIDC)") {
        When("토큰 발급 성공한 경우") {
            val restTemplate = TestRestTemplate()
            val clientId = "public-client"
            val clientSecret = registeredClientRepository.findByClientId(clientId)?.parseClientSecret()
            val redirectUrl = registeredClientRepository.findByClientId(clientId)?.redirectUris?.first()
            val authorizeUrl = "${authorizationServerSettings.issuer}/${authorizationServerSettings.authorizationEndpoint}"
            val tokenUrl = "${authorizationServerSettings.issuer}/${authorizationServerSettings.tokenEndpoint}"

            Then("access token 발급") {
                val session = restTemplate.login(port = port).headers.encodedSessionId()
                val code = restTemplate.oauth2Authorize(session, clientId, authorizeUrl, redirectUrl).headers.location.shouldNotBeNull()
                        .rawQuery.split("=").last()
                oauth2Token(clientId, clientSecret, tokenUrl, code, redirectUrl).shouldNotBeNull().should { tokenResponse ->
                    tokenResponse.accessToken.tokenType shouldBe OAuth2AccessToken.TokenType.BEARER
                    tokenResponse.accessToken.scopes.shouldNotBeEmpty()
                    tokenResponse.accessToken.tokenValue shouldNotBe null
                }
            }
            Then("refresh token 발급") {
                val session = restTemplate.login(port = port).headers.encodedSessionId()
                val code = restTemplate.oauth2Authorize(session, clientId, authorizeUrl, redirectUrl).headers.location.shouldNotBeNull()
                        .rawQuery.split("=").last()
                oauth2Token(clientId, clientSecret, tokenUrl, code, redirectUrl).shouldNotBeNull().refreshToken shouldNotBe null
            }

            Then("id token 발급") {
                val session = restTemplate.login(port = port).headers.encodedSessionId()
                val code = restTemplate.oauth2Authorize(session, clientId, authorizeUrl, redirectUrl).headers.location.shouldNotBeNull()
                        .rawQuery.split("=").last()
                oauth2Token(clientId, clientSecret, tokenUrl, code, redirectUrl).shouldNotBeNull().additionalParameters[ID_TOKEN] shouldNotBe null
            }
        }
    }
})