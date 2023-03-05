package com.service.authorization.login

import com.service.authorization.config.SecurityConstants
import com.service.authorization.config.TestSecurityConfig
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.string.shouldStartWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [TestSecurityConfig::class]
)
class LoginApiTests(
        @LocalServerPort
        private val port: Int
) : BehaviorSpec({
    val url = "http://localhost:$port"
    fun login(username: String = "user", password: String = "password"): ResponseEntity<String> {
        val testRestTemplate = TestRestTemplate()
        val headers = HttpHeaders().apply {
            this["content-type"] = "application/x-www-form-urlencoded"
        }
        val body = LinkedMultiValueMap<String, Any>().apply {
            this["username"] = username
            this["password"] = password
        }
        val entity = HttpEntity(body, headers)
        return testRestTemplate.exchange("$url/login", HttpMethod.POST, entity, String::class.java)
    }
    Given("로그인 하기") {
        When("회원이 존재하지 않는 경우") {
            Then("응답 헤더 location 이 로그인 경로") {
                val expected = "$url/login"
                val response: ResponseEntity<String> = login(username = "aa")
                val location = response.headers["Location"]?.first() ?: ""
                location shouldStartWith expected
            }
        }

        When("회원이 존재하고 비밀번호가 일치 하지 않는 경우") {
            Then("응답 헤더 location 이 로그인 경로") {
                val expected = "$url/login"
                val response: ResponseEntity<String> = login(password = "1")
                val location = response.headers["Location"]?.first() ?: ""
                location shouldStartWith expected
            }
        }

        When("회원이 존재하고 비밀번호가 일치 하는 경우") {
            Then("응답 헤더 location 이 로그인 경로") {
                val expected = "$url${SecurityConstants.SUCCESS_URL}"
                val response: ResponseEntity<String> = login()
                val location = response.headers["Location"]?.first() ?: ""
                location shouldStartWith expected
            }
        }
    }
})