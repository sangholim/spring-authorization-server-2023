package com.service.authorization.registeredClient

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient

class CustomRegisteredClientRepository(jdbcOperations: JdbcOperations) : JdbcRegisteredClientRepository(jdbcOperations) {

    private val COLUMN_NAMES = ("id, "
            + "client_id, "
            + "client_id_issued_at, "
            + "client_secret, "
            + "client_secret_expires_at, "
            + "client_name, "
            + "client_authentication_methods, "
            + "authorization_grant_types, "
            + "redirect_uris, "
            + "scopes, "
            + "client_settings,"
            + "token_settings")

    private val TABLE_NAME = "oauth2_registered_client"

    private val LOAD_REGISTERED_CLIENT_SQL = "SELECT $COLUMN_NAMES FROM $TABLE_NAME "

    private val DELETE_ALL = "DELETE FROM $TABLE_NAME"
    fun findBy(): List<RegisteredClient> {
        return jdbcOperations.query(LOAD_REGISTERED_CLIENT_SQL, registeredClientRowMapper)
    }

    fun deleteAll() {
        jdbcOperations.update(DELETE_ALL)
    }
}