package org.yinzhao.moneymaster.deals;

import org.hibernate.annotations.GenericGenerator;
import org.yinzhao.moneymaster.accounts.Account;
import org.yinzhao.moneymaster.category.DealCategory;
import org.yinzhao.moneymaster.enums.DealType;
import org.yinzhao.moneymaster.enums.EvtType;
import org.yinzhao.moneymaster.member.Member;
import org.yinzhao.moneymaster.utils.BalanceCaculator;
import org.yinzhao.moneymaster.utils.DBUtil;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Deal {

    private long id;


    private Date date;

    private Account account;


    private DealType type;


    private DealCategory category;

    private String detail;

    private BigDecimal amount;

    private BigDecimal curBalance;

    private Member member;

    private long relateID;

    public Deal() {

    }

    public Deal(Date date, Account account, DealType type, DealCategory category, String detail,
                BigDecimal amount, long relateID, BigDecimal curBalance, Member member) {
        this.date = date;
        this.account = account;
        this.type = type;
        this.category = category;
        this.detail = detail;
        this.amount = amount;
        this.relateID = relateID;
        this.curBalance = curBalance;
        this.member = member;
    }


    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "category")
    public DealCategory getCategory() {
        return category;
    }

    public void setCategory(DealCategory category) {
        this.category = category;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public long getId() {
        return id;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "day")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne
    @JoinColumn(name = "account")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Enumerated(EnumType.ORDINAL)
    public DealType getType() {
        return type;
    }

    public void setType(DealType type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCurBalance() {
        return curBalance;
    }

    public void setCurBalance(BigDecimal curBalance) {
        this.curBalance = curBalance;
    }

    @ManyToOne
    @JoinColumn(name = "member")
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public long getRelateID() {
        return relateID;
    }

    public void setRelateID(long relateID) {
        this.relateID = relateID;
    }

    public void updateCurBalance(EvtType type, Deal deal) {
        setCurBalance(BalanceCaculator.calBalance(account.getAccountType(), curBalance, deal, type));
        DBUtil.updateDeal(this);
    }
}
