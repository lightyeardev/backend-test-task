create table balance
(
    id            UUID           not null,
    version       BIGINT         not null,
    state         varchar(30)    not null,
    account_id    UUID           not null,
    amount        DECIMAL(20, 8) not null,
    currency      varchar(3)     not null,
    create_time   timestamp      not null,
    last_modified timestamp      not null,
    primary key (id)
);

CREATE INDEX IF NOT EXISTS IDX_BALANCE_ACCOUNT_ID ON balance(account_id);
