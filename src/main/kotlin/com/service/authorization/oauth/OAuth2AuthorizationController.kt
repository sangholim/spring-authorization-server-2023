package com.service.authorization.oauth

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class OAuth2AuthorizationController(
        private val authorizationService: CustomOAuth2AuthorizationService
) {

    @GetMapping("/console/register-clients/{id}/authorizations")
    fun getAll(@PathVariable id: String, model: Model): String {
        model.addAttribute("id", id)
        model.addAttribute("authorizations", authorizationService.findByRegisteredClientId(id).map { it.toView() })
        return "register-clients/authorization/main"
    }

}