package com.service.authorization.registeredClient

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RegisteredClientController(
        private val registeredClientService: RegisteredClientService
) {
    @GetMapping("/register-clients")
    fun getView(model: Model): String {
        model.addAttribute("clients", registeredClientService.getBy().map { it.toView() })
        return "register-client/main"
    }
}