package com.service.authorization.userFederatedIdentity

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

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

    @DeleteMapping("/console/users/{userId}/federated-identities")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable userId: String, @RequestParam("ids") ids: Set<String>?) {
        if (ids.isNullOrEmpty()) {
            return
        }
        userFederatedIdentityService.deleteByIds(userId, ids)
    }
}