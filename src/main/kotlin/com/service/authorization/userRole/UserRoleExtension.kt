package com.service.authorization.userRole

import org.springframework.security.core.authority.SimpleGrantedAuthority


fun UserRole.toGrantedAuthority() = SimpleGrantedAuthority(this.name.toString())