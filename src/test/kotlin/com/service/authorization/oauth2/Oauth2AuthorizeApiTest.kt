package com.service.authorization.oauth2

import com.service.authorization.client.login
import com.service.authorization.client.oauth2Authorize
import com.service.authorization.config.TestSecurityConfig
import com.service.authorization.htpHeader.encodedSessionId
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = [TestSecurityConfig::class]
)
class Oauth2AuthorizeApiTest(
        @LocalServerPort
        private val port: Int,
        private val registeredClientRepository: RegisteredClientRepository,
        private val authorizationServerSettings: AuthorizationServerSettings
) : BehaviorSpec({
    val restTemplate = TestRestTemplate()
    val clientId = "public-client"
    val redirectUrl = registeredClientRepository.findByClientId(clientId)?.redirectUris?.first()
    val authorizeUrl = "${authorizationServerSettings.issuer}/${authorizationServerSettings.authorizationEndpoint}"
    Given("oauth2 승인하기 요청") {
        When("서버에 승인 회원 정보가 인증된 경우") {
            Then("login url 로 redirect 된다") {
                val session = restTemplate.login(port = port).headers.encodedSessionId()
                restTemplate.oauth2Authorize(session, clientId, authorizeUrl, redirectUrl).headers.location.shouldNotBeNull().should { it ->
                    val uri = UriComponentsBuilder.fromUri(it).build()
                    uri.toUriString() shouldContain redirectUrl.shouldNotBeNull()
                    uri.queryParams[OAuth2ParameterNames.CODE] shouldNotBe null
                }
            }
        }
    }
})