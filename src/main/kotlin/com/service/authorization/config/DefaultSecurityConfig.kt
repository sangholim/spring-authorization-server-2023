package com.service.authorization.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.session.HttpSessionEventPublisher


@EnableWebSecurity
@Configuration
class DefaultSecurityConfig {

    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it.requestMatchers("/assets/**", "/webjars/**", "/login/template").permitAll()
                    .anyRequest().authenticated()
        }.formLogin(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun users(): UserDetailsService? {
        val user: UserDetails = User.withDefaultPasswordEncoder()
                .username("user1")
                .password("password")
                .roles("USER")
                .build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun sessionRegistry(): SessionRegistry? {
        return SessionRegistryImpl()
    }

    @Bean
    fun httpSessionEventPublisher(): HttpSessionEventPublisher? {
        return HttpSessionEventPublisher()
    }
}