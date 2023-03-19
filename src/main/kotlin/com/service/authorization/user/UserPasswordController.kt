package com.service.authorization.user

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class UserPasswordController(
        private val userService: UserService
) {

    @GetMapping("/console/users/{id}/password")
    fun get(@PathVariable id: String, model: Model): String {
        model.addAttribute("userId", id)
        return "users/password/main"
    }


    @PutMapping("/console/users/{id}/password")
    fun update(@PathVariable id: String, payload: UserPasswordUpdatePayload): String {
        userService.updatePassword(id, payload)
        return "redirect:/console/users/$id/password"
    }
}