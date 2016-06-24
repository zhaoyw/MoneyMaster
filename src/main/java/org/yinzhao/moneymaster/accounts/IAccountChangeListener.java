package org.yinzhao.moneymaster.accounts;

import org.yinzhao.moneymaster.enums.EvtType;

public interface IAccountChangeListener {
    void accountChanged(EvtType type, Account account);
}
