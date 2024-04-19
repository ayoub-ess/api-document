CREATE TABLE permission (
                       id INT PRIMARY KEY,
                       description VARCHAR(255) NOT NULL
);

insert into permission values (1, 'READ');
insert into permission values (2, 'WRITE');

CREATE TABLE document_share_permission (
                            id BIGSERIAL PRIMARY KEY,
                            user_id serial,
                            document_id serial,
                            permission_id int,
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (document_id) REFERENCES document(id),
                            FOREIGN KEY (permission_id) REFERENCES permission(id)
);