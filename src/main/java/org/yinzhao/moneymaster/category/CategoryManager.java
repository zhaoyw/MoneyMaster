package org.yinzhao.moneymaster.category;

import org.yinzhao.moneymaster.enums.DealType;
import org.yinzhao.moneymaster.enums.EvtType;
import org.yinzhao.moneymaster.utils.DBUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    private List<DealCategory> categoryList = new ArrayList<DealCategory>();
    private List<ICategoryChangeListener> listeners = new ArrayList<ICategoryChangeListener>();

    public CategoryManager() {
        loadDataFromDB();
    }

    private void loadDataFromDB() {
        List<DealCategory> categories = DBUtil.queryCategory();
        if (!categories.isEmpty()) {
            categoryList.addAll(categories);
        }
    }

    public void addCategory(DealCategory category) {
        if (!categoryList.contains(category)) {
            DBUtil.addCategory2DB(category);
            categoryList.add((category));
            fireCategoryChanged(EvtType.ADD, category);
        }
    }

    private void fireCategoryChanged(EvtType evtType, DealCategory category) {
        if (!listeners.isEmpty()) {
            for (ICategoryChangeListener listener : listeners) {
                listener.categoryChanged(evtType, category);
            }
        }
    }

    public void addListener(ICategoryChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public List<DealCategory> getCategoryList() {
        return this.categoryList;
    }

    public List<DealCategory> getCategoryList(DealType dealType) {
        List<DealCategory> list = new ArrayList<DealCategory>();
        for (DealCategory category : categoryList) {
            if (category.getType() == dealType) {
                list.add(category);
            }
        }
        return list;
    }

    public void clearListeners() {
        listeners.clear();
    }
}
