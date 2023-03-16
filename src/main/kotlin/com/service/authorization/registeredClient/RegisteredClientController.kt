package com.service.authorization.registeredClient

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping

@Controller
class RegisteredClientController(
        private val registeredClientService: RegisteredClientService
) {
    @GetMapping("/console/register-clients")
    fun getView(model: Model): String {
        model.addAttribute("clients", registeredClientService.getAllBy().map { it.toView() })
        return "register-clients/main"
    }

    @GetMapping("/console/register-clients/{id}")
    fun getDetailView(@PathVariable id: String, model: Model): String {
        model.addAttribute("client", registeredClientService.getBy(id).toDetailView())
        return "register-clients/detail"
    }

    @GetMapping("/console/register-clients/views/creation")
    fun getCreateView(): String =
            "register-clients/creation"

    @PostMapping("/console/register-clients", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun create(payload: RegisteredClientCreationPayload): String {
        registeredClientService.save(payload)
        return "redirect:/console/register-clients"
    }

    @PutMapping("/console/register-clients/{id}", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun update(@PathVariable id: String, payload: RegisteredClientUpdatePayload): String {
        registeredClientService.update(id, payload)
        return "redirect:/console/register-clients/$id"
    }
}