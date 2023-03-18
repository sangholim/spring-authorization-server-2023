package com.service.authorization.userRole

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

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
        model.addAttribute("roles", UserRoleType.values())
        return "users/roles/creation"
    }
}