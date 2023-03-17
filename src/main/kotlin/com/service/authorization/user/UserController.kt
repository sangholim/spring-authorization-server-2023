package com.service.authorization.user

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping

@Controller
class UserController(
        private val userService: UserService
) {

    @GetMapping("/console/users")
    fun getAll(model: Model): String {
        model.addAttribute("users", userService.getAll().map(User::toView))
        return "users/main"
    }

    @GetMapping("/console/users/views/creation")
    fun getCreateView(): String =
            "users/creation"

    @GetMapping("/console/users/{id}")
    fun get(@PathVariable id: String, model: Model): String {
        model.addAttribute("user", userService.getById(id)?.toView())
        return "users/detail"
    }

    @PutMapping("/console/users/{id}")
    fun update(@PathVariable id: String, payload: UserUpdatePayload): String {
        userService.update(id, payload)
        return "redirect:/console/users/$id"
    }

    @PostMapping("/console/users", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun save(payload: UserCreationPayload): String {
        userService.save(payload)
        return "redirect:/console/users"
    }
}