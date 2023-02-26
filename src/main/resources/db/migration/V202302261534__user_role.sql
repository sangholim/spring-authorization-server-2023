-- 회원 권한 테이블
CREATE TABLE user_role (
    id varchar(100) NOT NULL,
    user_id varchar(100) NOT NULL,
    role varchar(100) NOT NULL,
    PRIMARY KEY (id)
);