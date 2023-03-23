package com.service.authorization.oauth

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class OAuth2AuthorizationController(
        private val authorizationService: CustomOAuth2AuthorizationService
) {

    @GetMapping("/console/register-clients/{id}/authorizations")
    fun getAllByRegisteredClientId(@PathVariable id: String, model: Model): String {
        model.addAttribute("id", id)
        model.addAttribute("authorizations", authorizationService.findByRegisteredClientId(id).map { it.toView() })
        return "register-clients/authorizations/main"
    }

    @DeleteMapping("/console/register-clients/{registeredClientId}/authorizations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteByRegisteredClientId(@PathVariable registeredClientId: String, @RequestParam("ids") ids: Set<String>?) {
        if (ids.isNullOrEmpty()) {
            return
        }
        authorizationService.deleteBy(registeredClientId, ids)
    }

    @GetMapping("/console/users/{id}/authorizations")
    fun getAllByUserId(@PathVariable id: String, model: Model): String {
        model.addAttribute("id", id)
        model.addAttribute("authorizations", authorizationService.findByUserId(id).map { it.toView() })
        return "users/authorizations/main"
    }
}