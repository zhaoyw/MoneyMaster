package org.yinzhao.moneymaster.category;

import org.yinzhao.moneymaster.enums.EvtType;

public interface ICategoryChangeListener {
    void categoryChanged(EvtType type, DealCategory category);
}
