create table account
(
    id            UUID        not null,
    version       int         not null,
    name          varchar(32) not null,
    state         varchar(30) not null,
    create_time   timestamp   not null,
    last_modified timestamp   not null,
    primary key (id)
);
