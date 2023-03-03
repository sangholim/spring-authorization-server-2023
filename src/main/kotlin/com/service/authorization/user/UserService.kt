package com.service.authorization.user

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepository: UserRepository
) {

    fun save(userDetails: UserDetails): User = userRepository.save(User.of(userDetails))

    fun getBy(email: String): User? = userRepository.findByEmail(email)

    fun getById(id: String): User? = userRepository.findById(id).orElse(null)
}