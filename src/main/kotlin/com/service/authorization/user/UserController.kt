package com.service.authorization.user

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UserController(
        private val userService: UserService
) {

    @GetMapping("/console/users")
    fun getAll(model: Model): String {
        model.addAttribute("users", userService.getAll().map(User::toView))
        return "users/main"
    }
}