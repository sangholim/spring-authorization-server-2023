package com.service.authorization.login.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class TemplateController {

    @GetMapping("/login/template")
    fun index(): String = "index"
}