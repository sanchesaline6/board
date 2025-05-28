--liquibase formatted sql
--changeset aline:202505281339
--commnet: boards table create

CREATE TABLE boards (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

--rollback DROP TABLE BOARDS