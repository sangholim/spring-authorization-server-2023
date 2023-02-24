package com.service.authorization.jose

import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.RSAKey
import com.service.authorization.jose.KeyGeneratorUtils.generateEcKey
import com.service.authorization.jose.KeyGeneratorUtils.generateRsaKey
import com.service.authorization.jose.KeyGeneratorUtils.generateSecretKey
import java.security.KeyPair
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*


object Jwks {
    fun generateRsa(): RSAKey {
        val keyPair: KeyPair = generateRsaKey()
        val publicKey: RSAPublicKey = keyPair.public as RSAPublicKey
        val privateKey: RSAPrivateKey = keyPair.private as RSAPrivateKey
        return RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build()
    }

    fun generateEc(): ECKey {
        val keyPair: KeyPair = generateEcKey()
        val publicKey: ECPublicKey = keyPair.public as ECPublicKey
        val privateKey: ECPrivateKey = keyPair.private as ECPrivateKey
        val curve: Curve = Curve.forECParameterSpec(publicKey.params)
        return ECKey.Builder(curve, publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build()
    }

    fun generateSecret(): OctetSequenceKey {
        val secretKey = generateSecretKey()
        return OctetSequenceKey.Builder(secretKey)
                .keyID(UUID.randomUUID().toString())
                .build()
    }
}