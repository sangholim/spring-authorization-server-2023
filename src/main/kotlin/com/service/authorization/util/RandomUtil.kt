package com.service.authorization.util

object RandomUtil {

    private val PASSWORD_LENGTH = 11

    private val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun generatePassword(): String =
            List(PASSWORD_LENGTH) { charset.random() }.joinToString("")
}

