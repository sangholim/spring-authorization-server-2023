-- 세션 관리 테이블
CREATE TABLE session_management (
    id varchar(100) NOT NULL,
    timeout varchar(10) NOT NULL,
    flush_mode varchar(50) NOT NULL,
    PRIMARY KEY (id)
);
