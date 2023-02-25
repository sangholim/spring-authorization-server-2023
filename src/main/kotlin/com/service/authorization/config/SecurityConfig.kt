package com.service.authorization.config

import com.service.authorization.oauth.OAuth2AuthenticationSuccessHandler
import com.service.authorization.token.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain
import java.util.*


@EnableWebSecurity
@Configuration
class SecurityConfig(
        private val tokenProvider: TokenProvider,
        private val jwtDecoder: JwtDecoder
) {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeHttpRequests()
                .requestMatchers("", "/", "/login/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .successHandler(OAuth2AuthenticationSuccessHandler(tokenProvider))
                .loginPage("/templates/index.html")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint { _, response, _ ->
                    response.sendRedirect("/")
                }
                .and()
                .oauth2ResourceServer {
                    it.jwt().decoder(jwtDecoder)
                }
        return http.build()
    }

}
