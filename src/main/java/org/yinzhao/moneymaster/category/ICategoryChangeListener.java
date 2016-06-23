package org.yinzhao.moneymaster.category;

import org.yinzhao.moneymaster.enums.EvtType;

/**
 * Created by zhaoyongwang on 16/5/29.
 */
public interface ICategoryChangeListener {
    void categoryChanged(EvtType type, DealCategory category);
}
