package com.service.authorization.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String> {

    fun findByEmail(email: String): User?
}