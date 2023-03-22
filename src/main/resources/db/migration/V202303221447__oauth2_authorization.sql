-- oauth2 승인 정보 테이블
ALTER TABLE oauth2_authorization ALTER COLUMN authorization_code_issued_at TYPE TIMESTAMP WITH TIME ZONE;
ALTER TABLE oauth2_authorization ALTER COLUMN authorization_code_expires_at TYPE TIMESTAMP WITH TIME ZONE;
ALTER TABLE oauth2_authorization ALTER COLUMN access_token_issued_at TYPE TIMESTAMP WITH TIME ZONE;
ALTER TABLE oauth2_authorization ALTER COLUMN access_token_expires_at TYPE TIMESTAMP WITH TIME ZONE;
ALTER TABLE oauth2_authorization ALTER COLUMN oidc_id_token_issued_at TYPE TIMESTAMP WITH TIME ZONE;
ALTER TABLE oauth2_authorization ALTER COLUMN oidc_id_token_expires_at TYPE TIMESTAMP WITH TIME ZONE;
ALTER TABLE oauth2_authorization ALTER COLUMN refresh_token_issued_at TYPE TIMESTAMP WITH TIME ZONE;
ALTER TABLE oauth2_authorization ALTER COLUMN refresh_token_expires_at TYPE TIMESTAMP WITH TIME ZONE;
