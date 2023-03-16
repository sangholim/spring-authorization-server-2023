package com.service.authorization.registeredClient

import org.springframework.stereotype.Service

@Service
class RegisteredClientService(
        private val registeredClientRepository: CustomRegisteredClientRepository
) {

    fun getAllBy() = registeredClientRepository.findBy()

    fun getBy(id: String) = registeredClientRepository.findById(id) ?: throw Exception("존재하지 않는 client 입니다")

    fun save(payload: RegisteredClientCreationPayload) {
        if (registeredClientRepository.findByClientId(payload.clientId) != null) throw Exception("등록된 client-id 입니다")
        RegisteredClientFactory.create(payload).run(registeredClientRepository::save)
    }

    fun update(id: String, payload: RegisteredClientUpdatePayload) {
        val registeredClient = registeredClientRepository.findById(id) ?: throw Exception("존재하지 않는 id 입니다")
        registeredClient.update(payload).run(registeredClientRepository::save)
    }

    fun deleteByIds(ids: Set<String>) {
        ids.joinToString(separator = ",") { "'$it'" }.run(registeredClientRepository::deleteByIds)
    }
}