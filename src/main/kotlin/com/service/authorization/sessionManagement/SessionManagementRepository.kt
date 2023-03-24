package com.service.authorization.sessionManagement

import org.springframework.data.jpa.repository.JpaRepository

interface SessionManagementRepository: JpaRepository<SessionManagement, String>