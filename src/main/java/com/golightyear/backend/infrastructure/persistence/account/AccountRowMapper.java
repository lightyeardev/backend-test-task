package com.golightyear.backend.infrastructure.persistence.account;

import com.golightyear.backend.account.domain.Account;
import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.account.domain.AccountName;
import com.golightyear.backend.account.domain.AccountState;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {
    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Account.builder()
            .id(new AccountId(rs.getString("id")))
            .version(rs.getInt("version"))
            .name(new AccountName(rs.getString("name")))
            .state(AccountState.valueOf(rs.getString("state")))
            .createTime(rs.getTimestamp("create_time").toInstant())
            .lastModified(rs.getTimestamp("last_modified").toInstant())
            .build();
    }
}
