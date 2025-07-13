
CREATE TABLE users
(
    id                 SERIAL PRIMARY KEY NOT NULL,
    external_id        varchar(22)        NOT NULL,
    email              VARCHAR(300)       NOT NULL UNIQUE,
    password           VARCHAR(3000)      NOT NULL,
--  Use the varchar for enum to just simplify conversion on application level
    gender             varchar(30)          NOT NULL,
    is_activated       boolean            NOT NULL,
    is_email_confirmed boolean            NOT NULL,
    birthdate          date               NOT NULL
);