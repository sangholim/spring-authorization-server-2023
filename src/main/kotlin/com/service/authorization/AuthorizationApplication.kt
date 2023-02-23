package com.service.authorization

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthorizationApplication

fun main(args: Array<String>) {
	runApplication<AuthorizationApplication>(*args)
}
