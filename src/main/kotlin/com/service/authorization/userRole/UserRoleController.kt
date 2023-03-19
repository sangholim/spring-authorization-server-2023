package com.service.authorization.userRole

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class UserRoleController(
        private val userRoleService: UserRoleService
) {

    @GetMapping("/console/users/{userId}/roles")
    fun getAll(@PathVariable userId: String, model: Model): String {
        model.addAttribute("userId", userId)
        model.addAttribute("roles", userRoleService.getAllBy(userId))
        return "users/roles/main"
    }

    @GetMapping("/console/users/{userId}/roles/views/creation")
    fun getCreationView(@PathVariable userId: String, model: Model): String {
        model.addAttribute("userId", userId)
        model.addAttribute("roles", UserRoleName.values())
        return "users/roles/creation"
    }

    @PostMapping("/console/users/{userId}/roles", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun save(@PathVariable userId: String, payload: UserRoleCreationPayload): String {
        userRoleService.save(userId, payload)
        return "redirect:/console/users/${userId}/roles"
    }
}