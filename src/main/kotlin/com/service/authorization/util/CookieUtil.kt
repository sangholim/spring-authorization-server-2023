package com.service.authorization.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.SerializationUtils
import java.util.*


object CookieUtils {
    fun getCookie(request: HttpServletRequest, name: String): Cookie? {
        val cookies: Array<Cookie>? = request.cookies
        return cookies?.firstOrNull { it.name == name }
    }

    fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        Cookie(name, value).run {
            this.path = "/"
            this.isHttpOnly = true
            this.maxAge = maxAge
            response.addCookie(this)
        }
    }

    fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
        request.cookies?.firstOrNull { it.name == name }?.run {
            this.value = ""
            this.path = "/"
            this.maxAge = 0
            response.addCookie(this)
        }
    }

    fun serialize(`object`: Any): String {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(`object`))
    }

    fun <T> deserialize(cookie: Cookie, cls: Class<T>): T {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.value)))
    }
}
