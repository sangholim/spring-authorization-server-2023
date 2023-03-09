package com.service.authorization.registeredClient

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class RegisteredClientController {

    @GetMapping("/register-client")
    fun getView(modelAndView: ModelAndView): String {
        return "register-client/main"
    }
}