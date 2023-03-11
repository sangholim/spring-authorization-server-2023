package com.service.authorization.registeredClient

import org.springframework.stereotype.Service


@Service
class RegisteredClientService(
        private val registeredClientRepository: CustomRegisteredClientRepository
) {

    fun getAllBy() = registeredClientRepository.findBy()

    fun getBy(id: String) = registeredClientRepository.findById(id) ?: throw Exception("존재하지 않는 client 입니다")
}