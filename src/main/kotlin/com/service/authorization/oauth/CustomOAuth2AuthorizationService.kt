package com.service.authorization.oauth

import com.service.authorization.util.sqlValue
import com.service.authorization.util.sqlValues
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository

class CustomOAuth2AuthorizationService(
        jdbcOperations: JdbcOperations, registeredClientRepository: RegisteredClientRepository
) : JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository) {

    // @formatter:off
    private val COLUMN_NAMES = ("id, "
            + "registered_client_id, "
            + "principal_name, "
            + "authorization_grant_type, "
            + "authorized_scopes, "
            + "attributes, "
            + "state, "
            + "authorization_code_value, "
            + "authorization_code_issued_at, "
            + "authorization_code_expires_at,"
            + "authorization_code_metadata,"
            + "access_token_value,"
            + "access_token_issued_at,"
            + "access_token_expires_at,"
            + "access_token_metadata,"
            + "access_token_type,"
            + "access_token_scopes,"
            + "oidc_id_token_value,"
            + "oidc_id_token_issued_at,"
            + "oidc_id_token_expires_at,"
            + "oidc_id_token_metadata,"
            + "refresh_token_value,"
            + "refresh_token_issued_at,"
            + "refresh_token_expires_at,"
            + "refresh_token_metadata")
    // @formatter:on

    private val TABLE_NAME = "oauth2_authorization"

    private val SELECT_FROM = "SELECT $COLUMN_NAMES FROM $TABLE_NAME WHERE"

    private val DELETE_FROM = "DELETE FROM $TABLE_NAME"

    fun findByRegisteredClientId(registeredClientId: String): List<OAuth2Authorization> {
        val query = "$SELECT_FROM registered_client_id=${registeredClientId.sqlValue()}"

        return jdbcOperations.query(query, authorizationRowMapper)
    }

    fun deleteBy(registeredClientId: String, ids: Set<String>) {
        val query = "$DELETE_FROM WHERE registered_client_id=${registeredClientId.sqlValue()} AND id IN (${ids.sqlValues()})"
        jdbcOperations.update(query)
    }
}