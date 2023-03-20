package com.service.authorization.registeredClient

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class RegisteredClientTokenController(
        private val registeredClientService: RegisteredClientService
) {

    @GetMapping("/console/register-clients/{id}/token")
    fun getDetailView(@PathVariable id: String, model: Model): String {
        model.addAttribute("id", id)
        model.addAttribute("token", registeredClientService.getBy(id).tokenSettings.toView())
        return "register-clients/token/main"
    }
}