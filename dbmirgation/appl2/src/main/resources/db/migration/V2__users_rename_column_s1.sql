alter table users
    add column nick_name varchar(256);

update users
  set nick_name = name;
commit;