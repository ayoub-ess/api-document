create table document(
                         id Serial primary key,
                         documentUUID uuid,
                         name varchar(255) NOT NULL,
                         type varchar(10) NOT NULL,
                         creation_date timestamp default now()
);