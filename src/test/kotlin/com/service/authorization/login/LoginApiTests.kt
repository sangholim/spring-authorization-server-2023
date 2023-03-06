package com.service.authorization.login

import com.service.authorization.client.login
import com.service.authorization.config.TestSecurityConfig
import com.service.authorization.htpHeader.sessionId
import com.service.authorization.redis.authenticated
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.redis.core.RedisTemplate

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [TestSecurityConfig::class]
)
class LoginApiTests(
        @LocalServerPort
        private val port: Int,
        private val redisTemplate: RedisTemplate<String, String>
) : BehaviorSpec({
    val testRestTemplate = TestRestTemplate()

    Given("로그인 하기") {
        When("회원이 존재하지 않는 경우") {
            Then("응답 헤더 Location 은 '/login?error' 포함한다") {
                val sessionId = testRestTemplate.login(port = port, username = "aa").headers.sessionId()
                redisTemplate.authenticated(sessionId) shouldBe false
            }
        }

        When("회원이 존재하고 비밀번호가 일치 하지 않는 경우") {
            Then("응답 헤더 Location 은 '/login?error' 포함한다") {
                val sessionId = testRestTemplate.login(port = port, password = "1").headers.sessionId()
                redisTemplate.authenticated(sessionId) shouldBe false
            }
        }

        When("회원이 존재하고 비밀번호가 일치 하는 경우") {
            Then("응답 헤더 Location 은 '/console/frame' 포함한다") {
                val sessionId = testRestTemplate.login(port = port).headers.sessionId()
                redisTemplate.authenticated(sessionId) shouldBe true
            }
        }
    }
})