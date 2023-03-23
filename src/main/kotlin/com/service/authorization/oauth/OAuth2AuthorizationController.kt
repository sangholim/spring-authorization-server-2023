package com.service.authorization.oauth

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class OAuth2AuthorizationController(
        private val authorizationService: CustomOAuth2AuthorizationService
) {

    @GetMapping("/console/register-clients/{registeredClientId}/authorizations")
    fun getAllByRegisteredClientId(@PathVariable registeredClientId: String, model: Model): String {
        model.addAttribute("id", registeredClientId)
        model.addAttribute("authorizations", authorizationService.findByRegisteredClientId(registeredClientId).map { it.toView() })
        return "register-clients/authorizations/main"
    }

    @DeleteMapping("/console/register-clients/{registeredClientId}/authorizations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteByRegisteredClientId(@PathVariable registeredClientId: String, @RequestParam("ids") ids: Set<String>?) {
        if (ids.isNullOrEmpty()) {
            return
        }
        authorizationService.deleteByRegisteredClientId(registeredClientId, ids)
    }

    @GetMapping("/console/users/{userId}/authorizations")
    fun getAllByUserId(@PathVariable userId: String, model: Model): String {
        model.addAttribute("id", userId)
        model.addAttribute("authorizations", authorizationService.findByUserId(userId).map { it.toView() })
        return "users/authorizations/main"
    }

    @DeleteMapping("/console/users/{userId}/authorizations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteByUserId(@PathVariable userId: String, @RequestParam("ids") ids: Set<String>?) {
        if (ids.isNullOrEmpty()) {
            return
        }
        authorizationService.deleteByUserId(userId, ids)
    }
}