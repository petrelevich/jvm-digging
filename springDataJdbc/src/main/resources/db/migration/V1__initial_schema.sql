create table some_object
(
    id bigserial    not null
        constraint some_object_pk primary key,
    name varchar(256) not null,
    data varchar(256) not null
);

create table persistable_object
(
    id bigserial    not null
        constraint persistable_object_pk primary key,
    name varchar(256) not null,
    data varchar(256) not null
);
