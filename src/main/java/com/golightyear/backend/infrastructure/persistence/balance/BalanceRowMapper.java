package com.golightyear.backend.infrastructure.persistence.balance;

import com.golightyear.backend.account.domain.account.AccountId;
import com.golightyear.backend.account.domain.balance.Balance;
import com.golightyear.backend.account.domain.balance.BalanceAmount;
import com.golightyear.backend.account.domain.balance.BalanceId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class BalanceRowMapper implements RowMapper<Balance> {

    @Override
    public Balance mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Balance.builder()
            .id(new BalanceId(rs.getString("id")))
            .accountId(new AccountId(rs.getString("account_id")))
            .version(rs.getInt("version"))
            .amount(new BalanceAmount(rs.getBigDecimal("amount")))
            .createTime(rs.getTimestamp("create_time").toInstant())
            .lastModified(rs.getTimestamp("last_modified").toInstant())
            .build();
    }
}
