create table transaction
(
    id          UUID           not null,
    type        varchar(30)    not null,
    account_id  UUID           not null,
    amount      DECIMAL(20, 8) not null,
    currency    varchar(3)     not null,
    create_time timestamp      not null,
    primary key (id)
);

CREATE INDEX IF NOT EXISTS IDX_TRANSACTION_ACCOUNT_ID ON transaction (account_id);
CREATE INDEX IF NOT EXISTS IDX_TRANSACTION_TYPE ON transaction (type);
