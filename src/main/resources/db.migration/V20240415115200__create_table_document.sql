create table document(
                         documentUUID uuid primary key,
                         name varchar(255) NOT NULL,
                         type text NOT NULL,
                         creation_date timestamp default current_timestamp,
                         metadata JSONB
);