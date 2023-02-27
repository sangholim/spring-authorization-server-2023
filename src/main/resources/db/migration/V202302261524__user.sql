-- 회원 테이블
CREATE TABLE user_ (
    id varchar(100) NOT NULL,
    email varchar(100) UNIQUE NOT NULL,
    password varchar(100) NOT NULL,
    enabled BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);
