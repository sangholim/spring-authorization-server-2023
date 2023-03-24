package com.service.authorization.sessionManagement

import org.springframework.session.FlushMode

data class SessionManagementView(
        val timeout: Long,
        val flushMode: FlushMode
)

fun SessionManagement.toView() = SessionManagementView(
        timeout = timeout,
        flushMode = flushMode
)