package org.yinzhao.moneymaster.accounts;

import org.hibernate.annotations.GenericGenerator;
import org.yinzhao.moneymaster.deals.Deal;
import org.yinzhao.moneymaster.enums.AccountType;
import org.yinzhao.moneymaster.enums.EvtType;
import org.yinzhao.moneymaster.utils.BalanceCaculator;
import org.yinzhao.moneymaster.utils.DBUtil;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Account {

    private long id;

    private AccountType accountType;

    private String accountName;

    private BigDecimal balance;

    public Account() {

    }

    public Account(AccountType accountType, String accountName, BigDecimal balance) {
        this.accountType = accountType;
        this.accountName = accountName;
        this.balance = balance;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public long getId() {
        return id;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Column(name = "name")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return accountName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Account) {
            Account account = (Account) obj;
            return account.getId() == id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    public void updateBalanceByDeal(Deal deal, EvtType type) {
        setBalance(BalanceCaculator.calBalance(balance, deal, type));
        DBUtil.updateAccount(this);
    }
}
