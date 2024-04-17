ALTER TABLE metadata
    ADD COLUMN doc_id serial,
    ADD FOREIGN KEY (doc_id) REFERENCES document(id);