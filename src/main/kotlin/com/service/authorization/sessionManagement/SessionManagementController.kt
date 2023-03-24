package com.service.authorization.sessionManagement

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping

@Controller
class SessionManagementController(
        private val sessionManagementService: SessionManagementService
) {

    @GetMapping("/console/session-management")
    fun get(model: Model): String {
        model.addAttribute("sessionManagement", sessionManagementService.get().toView())
        return "session-management/main"
    }

    @PutMapping("/console/session-management")
    fun update(payload: SessionManagementUpdatePayload, model: Model): String {
        sessionManagementService.update(payload)
        return "redirect:/console/session-management"
    }
}