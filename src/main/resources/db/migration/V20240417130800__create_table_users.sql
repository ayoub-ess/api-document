CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       anneeAffectation INT
);

CREATE TABLE user_roles (
                            user_id BIGINT,
                            role VARCHAR(255),
                            FOREIGN KEY (user_id) REFERENCES users(id)
);