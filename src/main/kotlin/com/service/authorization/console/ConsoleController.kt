package com.service.authorization.console

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ConsoleController {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/console")
    fun getView(authentication: Authentication): String {
        logger.debug("auth: $authentication")
        return "console/main"
    }
}