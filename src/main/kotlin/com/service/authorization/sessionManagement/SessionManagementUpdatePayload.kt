package com.service.authorization.sessionManagement

import org.springframework.session.FlushMode

data class SessionManagementUpdatePayload(
        val timeout: Long,
        val flushMode: FlushMode
)