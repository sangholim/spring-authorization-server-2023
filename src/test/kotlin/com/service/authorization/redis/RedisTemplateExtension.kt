package com.service.authorization.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.session.data.redis.RedisSessionRepository.DEFAULT_KEY_NAMESPACE

/**
 * 인증 여부 확인
 */
fun RedisTemplate<String, String>.authenticated(sessionId: String): Boolean =
        opsForHash<String, String>().keys("$DEFAULT_KEY_NAMESPACE:sessions:$sessionId").contains("sessionAttr:SPRING_SECURITY_CONTEXT")
