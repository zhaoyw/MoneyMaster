package org.yinzhao.moneymaster.deals;

import org.yinzhao.moneymaster.enums.EvtType;

/**
 * Created by zhaoyongwang on 16/5/31.
 */
public interface IDealChangeListener {
    void dealChanged(EvtType type, Deal deal);
}
