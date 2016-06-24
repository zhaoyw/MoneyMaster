package org.yinzhao.moneymaster.ui;

import org.yinzhao.moneymaster.accounts.Account;
import org.yinzhao.moneymaster.accounts.AccountManager;
import org.yinzhao.moneymaster.accounts.IAccountChangeListener;
import org.yinzhao.moneymaster.enums.DialogReturnMode;
import org.yinzhao.moneymaster.enums.EvtType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountManagerPanel extends JPanel implements IAccountChangeListener {

    public AccountManagerPanel(AccountManager manager) {
        this.manager = manager;
        this.manager.clearListeners();
        this.manager.addListener(this);
        initUI();
        loadData();
    }

    private void initUI() {
        String[] colNames = new String[]{"SequenceID", "Name", "Type", "Balance"};
        tableModel = new DefaultTableModel(colNames, 0);
        accountTable = new JTable(tableModel);

        setLayout(new GridBagLayout());
        add(new JScrollPane(accountTable), new GridBagConstraints(0, 0, 1, 1, 1.0d, 1.0d, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));

        JPanel btnPanel = createBtnPanel();
        add(btnPanel, new GridBagConstraints(0, 1, 1, 1, 0.0d, 0.0d, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(6, 6, 6, 6), 0, 0));
    }

    private JPanel createBtnPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));

        addBtn = new JButton("Add Account");
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TAccountDialog dialog = new TAccountDialog(null);
                dialog.setVisible(true);
                if (dialog.getReturnMode() == DialogReturnMode.RETURN_OK) {
                    Account account = dialog.getAccount();
                    manager.addAccount(account);
                }
            }
        });
        delBtn = new JButton("Delete Account");
        delBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedIndices = accountTable.getSelectedRows();
                if (selectedIndices.length > 0) {
                    //TODO
                }
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(delBtn);

        return btnPanel;
    }

    private void loadData() {
        java.util.List<Account> accountList = manager.getAccountList();
        if (!accountList.isEmpty()) {
            for (Account account : accountList) {
                addAccount2Table(account);
            }
        }
    }

    private void addAccount2Table(Account account) {
        Object[] rowData = new Object[]{seq++, account.getAccountName(), account.getAccountType(), account.getBalance()};
        tableModel.addRow(rowData);
        accountTable.revalidate();
    }

    public void accountChanged(EvtType type, Account account) {
        if (type != null && account != null) {
            if (type == EvtType.ADD) {
                addAccount2Table(account);
            }
        }
    }

    private AccountManager manager;

    private JTable accountTable;
    private DefaultTableModel tableModel;
    private JButton addBtn;
    private JButton delBtn;

    long seq = 1;
}
