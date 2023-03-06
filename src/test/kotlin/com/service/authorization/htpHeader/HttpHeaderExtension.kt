package com.service.authorization.htpHeader

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.util.Base64Utils

/**
 * format
 * set-cookie: SESSION=xxxx;a=b
 */
fun HttpHeaders.encodedSessionId() =
        this[SET_COOKIE]!!.first().split(";").first { it.startsWith("SESSION") }.split("=").last()

fun HttpHeaders.sessionId() = String(Base64Utils.decodeFromString(encodedSessionId()))