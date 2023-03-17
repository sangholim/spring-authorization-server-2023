package com.service.authorization.user

import com.service.authorization.user.User.Companion.user
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepository: UserRepository
) {

    fun save(userDetails: UserDetails): User = userRepository.save(User.of(userDetails))

    fun save(payload: UserCreationPayload): User {
        if (userRepository.findByEmail(payload.email) != null) throw Exception("가입된 이메일입니다")

        return user {
            email = payload.email
            password = payload.password
            enabled = true
        }.run(userRepository::save)
    }

    fun getBy(email: String): User? = userRepository.findByEmail(email)

    fun getById(id: String): User? = userRepository.findById(id).orElse(null)

    fun getAll(): List<User> = userRepository.findAll()

    fun update(id: String, payload: UserUpdatePayload) {
        val user = userRepository.findByIdOrNull(id) ?: throw Exception("존재하지 않는 회원 id 입니다")
        if (user.email != payload.email && userRepository.findByEmail(payload.email) != null) throw Exception("사용중인 회원 이메일 입니다")
        user.update(payload).run(userRepository::save)
    }

    fun deleteByIds(ids: Set<String>) {
        userRepository.deleteAllById(ids)
    }
}