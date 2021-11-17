create table account_transaction
(
    id            UUID          not null,
    type          varchar(50)   not null,
    account_id    UUID          not null,
    amount        decimal(19,8) not null,
    create_time   timestamp     not null,
    primary key (id)
);

create index idx_account_transaction_account_id on account_transaction (account_id);
