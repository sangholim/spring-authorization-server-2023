package com.service.authorization.registeredClient

import org.springframework.stereotype.Service


@Service
class RegisteredClientService(
        private val registeredClientRepository: CustomRegisteredClientRepository
) {

    fun getBy() = registeredClientRepository.findBy()
}