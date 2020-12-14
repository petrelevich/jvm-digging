create table some_object
(
    id   bigserial    not null
        constraint some_object_pk primary key,
    name varchar(256) not null,
    data varchar(256) not null
);

create table persistable_object
(
    id   bigserial    not null
        constraint persistable_object_pk primary key,
    name varchar(256) not null,
    data varchar(256) not null
);

create table record_package
(
    record_package_id bigserial    not null
        constraint record_package_pk primary key,
    name              varchar(256) not null
);

create table record
(
    record_id         bigserial    not null
        constraint record_pk primary key,
    record_package_id bigint       not null,
    data              varchar(256) not null
);

alter table record
    add foreign key (record_package_id) references record_package;


create table info_main
(
    info_main_id bigserial    not null
        constraint info_pk primary key,
    main_data    varchar(256) not null
);

create table info_additional
(
    info_additional_id bigserial    not null
        constraint additional_pk primary key,
    info_main_id       bigint       not null,
    additional_data    varchar(256) not null
);

alter table info_additional
    add foreign key (info_main_id) references info_main;

---------------------------------------
create table owner
(
    owner_name varchar(256) primary key,
    address    varchar(500) not null
);

create table dog
(
    dog_id     bigserial primary key,
    name       varchar(200) not null,
    owner_name varchar(256)
);

alter table dog
    add foreign key (owner_name) references owner (owner_name);