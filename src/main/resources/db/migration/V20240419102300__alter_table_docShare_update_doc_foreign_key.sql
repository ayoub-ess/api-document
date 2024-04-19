ALTER TABLE document_share_permission
    DROP CONSTRAINT document_share_permission_document_id_fkey,
    ADD CONSTRAINT document_share_permission_document_id_fkey
        FOREIGN KEY (document_id)
            REFERENCES document(id)
            ON DELETE CASCADE;