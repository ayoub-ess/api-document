create table document(
                         id bigint primary key,
                         documentUUID uuid,
                         name varchar(255) NOT NULL,
                         type varchar(10) NOT NULL,
                         creation_date timestamp default current_timestamp
);