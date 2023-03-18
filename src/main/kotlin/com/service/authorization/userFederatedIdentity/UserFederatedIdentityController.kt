package com.service.authorization.userFederatedIdentity

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class UserFederatedIdentityController(
        private val userFederatedIdentityService: UserFederatedIdentityService
) {

    @GetMapping("/console/users/{userId}/federated-identities")
    fun getAll(@PathVariable userId: String, model: Model): String {
        model.addAttribute("userId", userId)
        model.addAttribute("federatedIdentities", userFederatedIdentityService.getAllBy(userId))
        return "users/federated-identities/main"
    }

}