package com.service.authorization.token

import com.nimbusds.jose.jwk.RSAKey
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors

class TokenProvider(
        private val rsaKey: RSAKey,
        private val jwtEncoder: JwtEncoder
) {

    fun createToken(authentication: Authentication): String {
        val jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).keyId(rsaKey.keyID).build()
        val now: Instant = Instant.now()
        val scope: String = authentication.authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "))
        val claims: JwtClaimsSet = JwtClaimsSet.builder()
                .issuer("발급서버주소")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.name)
                .audience(listOf("수신서버주소"))
                .claim("scope", scope)
                .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }
}