package com.service.authorization.registeredClient

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping

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

    @PutMapping("/console/register-clients/{id}/token", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun update(@PathVariable id: String, payload: RegisteredClientTokenUpdatePayload): String {
        registeredClientService.updateToken(id, payload)
        return "redirect:/console/register-clients/$id/token"
    }
}