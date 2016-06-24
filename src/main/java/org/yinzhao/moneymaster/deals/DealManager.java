package org.yinzhao.moneymaster.deals;

import org.yinzhao.moneymaster.enums.EvtType;
import org.yinzhao.moneymaster.utils.DBUtil;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DealManager {
    private List<Deal> dealList = new ArrayList<Deal>();
    private List<IDealChangeListener> listeners = new ArrayList<IDealChangeListener>();

    public DealManager() {
        loadDataFromDB();
    }

    private void loadDataFromDB() {
        List<Deal> deals = DBUtil.queryDeals();
        if (!deals.isEmpty()) {
            dealList.addAll(deals);
        }
    }

    public void addDeal(Deal deal) {
        if (!dealList.contains(deal)) {
            DBUtil.addDeal2DB(deal);
            dealList.add((deal));
            fireAccountChanged(EvtType.ADD, deal);
        }
    }

    private void fireAccountChanged(EvtType evtType, Deal deal) {
        if (!listeners.isEmpty()) {
            for (IDealChangeListener listener : listeners) {
                listener.dealChanged(evtType, deal);
            }
        }
    }

    public void addListener(IDealChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public List<Deal> getDealList() {
        return this.dealList;
    }

    public void delDealByID(Long dbID) {
        Deal toDelDeal = null;
        for (Deal deal : dealList) {
            if (deal.getId() == dbID.longValue()) {
                toDelDeal = deal;
                break;
            }
        }

        if (toDelDeal != null) {
            DBUtil.delDeal(toDelDeal);
            dealList.remove(toDelDeal);
            fireAccountChanged(EvtType.DEL, toDelDeal);

            for (Deal deal : dealList) {
                if (deal.getId() > dbID.longValue()) {
                    deal.updateCurBalance(EvtType.DEL, toDelDeal);
                    fireAccountChanged(EvtType.MOD, deal);
                }
            }
        }
    }

    public void clearListeners() {
        listeners.clear();
    }

    public Deal getDealByID(long dbID) {
        for (Deal deal : dealList) {
            if (deal.getId() == dbID) {
                return deal;
            }
        }
        return null;
    }
}
