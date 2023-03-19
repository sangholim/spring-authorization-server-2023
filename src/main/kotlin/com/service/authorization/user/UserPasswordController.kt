package com.service.authorization.user

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class UserPasswordController(
        private val userService: UserService
) {

    @GetMapping("/console/users/{userId}/password")
    fun get(@PathVariable userId: String, model: Model): String {
        model.addAttribute("userId", userId)
        return "users/password/main"
    }
}