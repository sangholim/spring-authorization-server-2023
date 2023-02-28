package com.service.authorization.userFederatedIdentity

import org.springframework.data.jpa.repository.JpaRepository

interface UserFederatedIdentityRepository: JpaRepository<UserFederatedIdentity, String> {

}