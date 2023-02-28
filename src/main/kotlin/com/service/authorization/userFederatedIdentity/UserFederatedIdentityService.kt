package com.service.authorization.userFederatedIdentity

import org.springframework.stereotype.Service

@Service
class UserFederatedIdentityService(
        private val userFederatedIdentityRepository: UserFederatedIdentityRepository
) {

    fun save(id: String, userId: String, provider: String) =
            userFederatedIdentityRepository.save(
                    UserFederatedIdentity.of(id, userId, provider)
            )

    fun getBy(id: String): UserFederatedIdentity? = userFederatedIdentityRepository.findById(id).orElse(null)

    fun getOrSave(id: String, userId: String, provider: String): UserFederatedIdentity =
            getBy(id) ?: save(id, userId, provider)
}