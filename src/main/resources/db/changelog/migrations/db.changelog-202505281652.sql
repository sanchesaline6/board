--liquibase formatted sql
--changeset aline:202505281339
--commnet: cards table create

CREATE TABLE cards (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    "order" INT NOT NULL,
    board_column_id INT NOT NULL,
    CONSTRAINT boards_columns__cards_fk FOREIGN KEY (board_column_id) REFERENCES boards_columns(id) ON DELETE CASCADE
);

--rollback DROP TABLE cards