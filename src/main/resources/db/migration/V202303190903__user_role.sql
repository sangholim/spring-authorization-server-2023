-- 회원 권한 테이블
-- name column 추가
ALTER TABLE user_role ADD COLUMN name varchar(100) NOT NULL;
-- role column 제거
ALTER TABLE user_role DROP role;