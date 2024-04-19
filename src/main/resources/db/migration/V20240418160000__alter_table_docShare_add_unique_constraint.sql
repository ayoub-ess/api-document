ALTER TABLE document_share_permission
    ADD CONSTRAINT unique_document_share_permission UNIQUE (user_id, document_id, permission_id);
