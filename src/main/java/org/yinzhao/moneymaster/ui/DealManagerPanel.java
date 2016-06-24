package org.yinzhao.moneymaster.ui;

import org.yinzhao.moneymaster.accounts.AccountManager;
import org.yinzhao.moneymaster.category.CategoryManager;
import org.yinzhao.moneymaster.deals.Deal;
import org.yinzhao.moneymaster.deals.DealManager;
import org.yinzhao.moneymaster.deals.IDealChangeListener;
import org.yinzhao.moneymaster.enums.DialogReturnMode;
import org.yinzhao.moneymaster.enums.EvtType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class DealManagerPanel extends JPanel implements IDealChangeListener {
    private final DealManager dealManager;
    private final AccountManager accountManager;
    private final CategoryManager categoryManager;
    private JTable dealTable;
    private DefaultTableModel tableModel;
    private JButton addBtn;
    private JButton delBtn;
    private Map<Long, Long> seqID2DBIDMap = new HashMap<Long, Long>();

    long seq = 1;

    public DealManagerPanel(DealManager dealManager, AccountManager accountManager, CategoryManager categoryManager) {
        this.dealManager = dealManager;
        this.accountManager = accountManager;
        this.categoryManager = categoryManager;
        dealManager.clearListeners();
        dealManager.addListener(this);
        initUI();
        loadData();
    }

    private void loadData() {
        java.util.List<Deal> dealList = dealManager.getDealList();
        if (!dealList.isEmpty()) {
            for (Deal deal : dealList) {
                addDeal2Table(deal);
            }
        }
    }

    private void addDeal2Table(Deal deal) {
        seqID2DBIDMap.put(seq, deal.getId());
        Object[] rowData = new Object[]{seq, deal.getDate(), deal.getAccount(), deal.getType(), deal.getCategory(),
                deal.getDetail(), deal.getAmount(), deal.getCurBalance()};
        seq++;
        tableModel.addRow(rowData);
        dealTable.revalidate();
    }

    private void initUI() {
        String[] colNames = new String[]{"SequenceID", "Date", "Account", "DealType", "Category", "Detail", "Amount",
                "Balance"};
        tableModel = new DefaultTableModel(colNames, 0);
        dealTable = new JTable(tableModel);

        setLayout(new GridBagLayout());
        add(new JScrollPane(dealTable), new GridBagConstraints(0, 0, 1, 1, 1.0d, 1.0d, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));

        JPanel btnPanel = createBtnPanel();
        add(btnPanel, new GridBagConstraints(0, 1, 1, 1, 0.0d, 0.0d, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(6, 6, 6, 6), 0, 0));
    }

    private JPanel createBtnPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));

        addBtn = new JButton("Add Deal");
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DealDialog dialog = new DealDialog(null, accountManager, categoryManager);
                dialog.setVisible(true);
                if (dialog.getReturnMode() == DialogReturnMode.RETURN_OK) {
                    Deal deal = dialog.getDeal();
                    dealManager.addDeal(deal);
                }
            }
        });
        delBtn = new JButton("Delete Deal");
        delBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedIndices = dealTable.getSelectedRows();
                if (selectedIndices.length > 0 && selectedIndices.length == 1) {
                    int modelIndex = dealTable.convertRowIndexToModel(selectedIndices[0]);
                    if (modelIndex != -1) {
                        Long seqId = (Long) dealTable.getValueAt(modelIndex, 0);
                        if (seqID2DBIDMap.containsKey(seqId)) {
                            Long dbID = seqID2DBIDMap.get(seqId);
                            accountManager.updateBalance(dealManager.getDealByID(dbID), EvtType.DEL);
                            dealManager.delDealByID(dbID);
                        }
                    }
                }
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(delBtn);

        return btnPanel;
    }

    public void dealChanged(EvtType type, Deal deal) {
        if (deal != null) {
            if (type == EvtType.ADD) {
                addDeal2Table(deal);
            } else if (type == EvtType.DEL){
                delDeal2Table(deal);
            } else if (type == EvtType.MOD) {
                modDeal2Table(deal);
            }
        }
    }

    private void modDeal2Table(Deal deal) {
        if (deal != null) {
            long seqID = findSeqID(deal);
            if (seqID != -1) {
                int index = findTableIndex(seqID);
                if (index != -1) {
                    tableModel.setValueAt(deal.getCurBalance(), index, 7);
                }
            }
        }
    }

    private void delDeal2Table(Deal deal) {
        if (deal != null) {
            long delSeqId = findSeqID(deal);
            if (delSeqId != -1) {
                int removeIndex = findTableIndex(delSeqId);
                if (removeIndex != -1) {
                    tableModel.removeRow(removeIndex);
                    tableModel.fireTableRowsDeleted(removeIndex, removeIndex);
                }
            }
        }
    }

    private int findTableIndex(long delSeqId) {
        int removeIndex = -1;
        for (int i = 0, count = tableModel.getRowCount(); i < count; ++i) {
            if (((Long) tableModel.getValueAt(i, 0)).longValue() == delSeqId) {
                removeIndex = i;
                break;
            }
        }
        return removeIndex;
    }

    private long findSeqID(Deal deal) {
        long delSeqId = -1;
        for (Long id : seqID2DBIDMap.keySet()) {
            if (seqID2DBIDMap.get(id).longValue() == deal.getId()) {
                delSeqId = id.longValue();
                break;
            }
        }
        return delSeqId;
    }
}
