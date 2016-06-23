package org.yinzhao.moneymaster.accounts;

import org.yinzhao.moneymaster.deals.Deal;
import org.yinzhao.moneymaster.enums.EvtType;
import org.yinzhao.moneymaster.utils.DBUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyongwang on 16/5/28.
 */
public class AccountManager {
    private List<Account> accountList = new ArrayList<Account>();
    private List<IAccountChangeListener> listeners = new ArrayList<IAccountChangeListener>();

    public AccountManager() {
        loadAccountFromDB();
    }

    private void loadAccountFromDB() {
        List<Account> accounts = DBUtil.queryAccount();
        if (!accounts.isEmpty()) {
            accountList.addAll(accounts);
        }
    }

    public void addAccount(Account account) {
        if (!accountList.contains(account)) {
            DBUtil.addAccount2DB(account);
            accountList.add((account));
            fireAccountChanged(EvtType.ADD, account);
        }
    }

    private void fireAccountChanged(EvtType evtType, Account account) {
        if (!listeners.isEmpty()) {
            for (IAccountChangeListener listener : listeners) {
                listener.accountChanged(evtType, account);
            }
        }
    }

    public void removeAccount(Account account) {
        if (accountList.contains(account)) {
            //TODO fire del data in DB
            accountList.remove(account);
        }
    }

    public List<Account> getAccountList() {
        return this.accountList;
    }

    public void addListener(IAccountChangeListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void updateBalance(Deal deal, EvtType type) {
        if (deal != null) {
            for (Account account : accountList) {
                if (account.equals(deal.getAccount())) {
                    account.updateBalanceByDeal(deal, type);
                    break;
                }
            }
        }
    }

    public void clearListeners() {
        listeners.clear();
    }
}
