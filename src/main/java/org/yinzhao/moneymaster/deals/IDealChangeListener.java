package org.yinzhao.moneymaster.deals;

import org.yinzhao.moneymaster.enums.EvtType;

public interface IDealChangeListener {
    void dealChanged(EvtType type, Deal deal);
}
