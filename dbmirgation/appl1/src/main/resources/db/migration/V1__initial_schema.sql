create table users
(
    id  bigserial primary key,
    name varchar(256) not null unique
);