create table ledger
(
    id              UUID           not null,
    debitAccountId  UUID,
    creditAccountId UUID,
    operation       varchar(30)    not null,
    amount          numeric(18, 3) not null,
    create_time     timestamp      not null,
    primary key (id),
    constraint fk_debit_account foreign key (debitAccountId) references account (id),
    constraint fk_credit_account foreign key (creditAccountId) references account (id)
);
