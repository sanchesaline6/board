--liquibase formatted sql
--changeset aline:202505281339
--commnet: boards_columns table create

CREATE TABLE boards_columns (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    "order" INT NOT NULL,
    kind VARCHAR(7) NOT NULL,
    board_id INT NOT NULL,
    CONSTRAINT boards__boards_columns_fk FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
    CONSTRAINT id_order_uk UNIQUE (board_id, "order")
);

--rollback DROP TABLE boards_columns