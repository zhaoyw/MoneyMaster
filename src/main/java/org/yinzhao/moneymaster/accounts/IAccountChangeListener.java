package org.yinzhao.moneymaster.accounts;

import org.yinzhao.moneymaster.enums.EvtType;

/**
 * Created by zhaoyongwang on 16/5/28.
 */
public interface IAccountChangeListener {
    void accountChanged(EvtType type, Account account);
}
