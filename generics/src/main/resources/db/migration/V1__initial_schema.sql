create table message
(
    id       bigserial    not null primary key,
    room_id  varchar(50)  not null,
    msg_text varchar(500) not null
);
create index idx_message_room_id on message (room_id);
