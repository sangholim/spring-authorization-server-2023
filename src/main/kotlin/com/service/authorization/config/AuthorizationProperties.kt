package com.service.authorization.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "authorization")
data class AuthorizationProperties(
        val issuer: String
)