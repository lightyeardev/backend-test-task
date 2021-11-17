create table balance
(
    id            UUID          not null,
    account_id    UUID          not null,
    version       int           not null,
    amount        decimal(19,8) not null,
    create_time   timestamp     not null,
    last_modified timestamp     not null,
    primary key (id)
);

create index idx_balance_account_id on balance (account_id);
