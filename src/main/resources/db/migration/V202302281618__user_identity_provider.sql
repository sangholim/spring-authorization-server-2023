-- 회원 연동 id 테이블
CREATE TABLE user_federated_identity (
    id varchar(100) UNIQUE NOT NULL,
    user_id varchar(100) NOT NULL,
    registration_id varchar(100) NOT NULL,
    PRIMARY KEY (id)
);