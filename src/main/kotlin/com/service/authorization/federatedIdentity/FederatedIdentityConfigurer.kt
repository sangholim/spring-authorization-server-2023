package com.service.authorization.federatedIdentity

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint

class FederatedIdentityConfigurer : AbstractHttpConfigurer<FederatedIdentityConfigurer, HttpSecurity>() {
    override fun init(http: HttpSecurity) {
        http
                .exceptionHandling { exceptions ->
                    exceptions
                            .authenticationEntryPoint(
                                    LoginUrlAuthenticationEntryPoint("/login"))
                }
                .oauth2Login()
    }
}