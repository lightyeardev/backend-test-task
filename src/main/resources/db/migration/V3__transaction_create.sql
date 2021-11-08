create table transaction
(
    id           UUID           not null,
    from_account UUID           not null,
    to_account   UUID           not null,
    amount       NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
    create_time  timestamp      not null,
    primary key (id),
    CONSTRAINT fk_from_account
        FOREIGN KEY (from_account)
            REFERENCES account (id),
    CONSTRAINT fk_to_account
        FOREIGN KEY (to_account)
            REFERENCES account (id)
);