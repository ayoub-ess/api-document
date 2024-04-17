ALTER TABLE document
    ADD COLUMN user_id BIGSERIAL,
    ADD FOREIGN KEY (user_id) REFERENCES users(id);
