create type PAYMENT_STATUS as enum ('NEW', 'DONE', 'ERROR');
create cast (character varying as PAYMENT_STATUS) with inout as assignment;

create table payments
(
    id               bigserial   not null primary key,
    card_account     varchar(50) not null,
    amount           numeric(8, 2) not null,
    status           PAYMENT_STATUS not null,
    created_at       timestamp without time zone not null,
    updated_at       timestamp without time zone not null
);

create or replace function create_payment(card_account varchar, amount numeric)
    returns bigint
    language plpgsql
    as
$$
declare
    created_id payments.id%type;
    now        payments.created_at%type;
begin
    now = now() at time zone 'utc';

    insert into payments(card_account, amount, status, created_at, updated_at)
       values (card_account, amount, 'NEW', now, now)
       returning id into created_id;

    return created_id;
end;
$$