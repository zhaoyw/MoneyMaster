package org.yinzhao.moneymaster.ui;

import org.yinzhao.moneymaster.accounts.Account;
import org.yinzhao.moneymaster.accounts.AccountManager;
import org.yinzhao.moneymaster.category.CategoryManager;
import org.yinzhao.moneymaster.category.DealCategory;
import org.yinzhao.moneymaster.deals.Deal;
import org.yinzhao.moneymaster.enums.DealType;
import org.yinzhao.moneymaster.enums.DialogReturnMode;
import org.yinzhao.moneymaster.enums.EvtType;
import org.yinzhao.moneymaster.utils.BalanceCaculator;
import org.yinzhao.moneymaster.utils.UIToolKit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Vector;

public class DealDialog extends JDialog {
    private final AccountManager accountManager;
    private final CategoryManager categoryManager;
    private DialogReturnMode returnMode = DialogReturnMode.RETURN_CANCEL;
    private Deal deal = null;
    private NumberFormat format = NumberFormat.getNumberInstance();

    private JSpinner dateSpinner;
    private JTextField detailFld;
    private JFormattedTextField amountFld;
    private JComboBox accountBox;
    private JComboBox dealTypeBox;
    private JComboBox categoryBox;

    public DealDialog(Window parent, AccountManager accountManager, CategoryManager categoryManager) {
        super(parent);
        this.accountManager = accountManager;
        this.categoryManager = categoryManager;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());

        add(new JLabel("Date"), new GridBagConstraints(0, 0, 1, 1, 0.0d, 0.0d, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));

        //Add the third label-spinner pair.
        Calendar calendar = Calendar.getInstance();
        java.util.Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -100);
        java.util.Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 200);
        java.util.Date latestDate = calendar.getTime();
        SpinnerModel dateModel = new SpinnerDateModel(initDate,
                earliestDate,
                latestDate,
                Calendar.YEAR);//ignored for user input
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy/MM/dd"));
        dateSpinner.setPreferredSize(new Dimension(100, 25));
        add(dateSpinner, new GridBagConstraints(1, 0, 1, 1, 1.0d, 1.0d, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(6, 0, 6, 6), 0, 0));

        add(new JLabel("Account"), new GridBagConstraints(0, 1, 1, 1, 0.0d, 0.0d, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        accountBox = new JComboBox(new DefaultComboBoxModel(new Vector<Account>(accountManager.getAccountList())));
        add(accountBox, new GridBagConstraints(1, 1, 1, 1, 1.0d, 1.0d, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(6, 0, 6, 6), 0, 0));

        add(new JLabel("Deal Type"), new GridBagConstraints(0, 2, 1, 1, 0.0d, 0.0d, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        dealTypeBox = new JComboBox(new DefaultComboBoxModel(DealType.values()));
        add(dealTypeBox, new GridBagConstraints(1, 2, 1, 1, 1.0d, 1.0d, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(6, 0, 6, 6), 0, 0));

        add(new JLabel("Deal Category"), new GridBagConstraints(0, 3, 1, 1, 0.0d, 0.0d, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        categoryBox = new JComboBox(new DefaultComboBoxModel(new Vector<DealCategory>(categoryManager.getCategoryList(DealType.IN))));
        add(categoryBox, new GridBagConstraints(1, 3, 1, 1, 1.0d, 1.0d, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(6, 0, 6, 6), 0, 0));

        add(new JLabel("Detail"), new GridBagConstraints(0, 4, 1, 1, 0.0d, 0.0d, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        detailFld = new JTextField();
        detailFld.setPreferredSize(new Dimension(100, 25));
        add(detailFld, new GridBagConstraints(1, 4, 1, 1, 1.0d, 1.0d, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(6, 0, 6, 6), 0, 0));

        add(new JLabel("Amount"), new GridBagConstraints(0, 5, 1, 1, 0.0d, 0.0d, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));

        format.setMaximumFractionDigits(2);
        amountFld = new JFormattedTextField(format);
        amountFld.setPreferredSize(new Dimension(100, 25));
        add(amountFld, new GridBagConstraints(1, 5, 1, 1, 1.0d, 1.0d, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(6, 0, 6, 6), 0, 0));

        dealTypeBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    DealType type = (DealType) dealTypeBox.getSelectedItem();
                    refreshCategoryBox(type);
                }
            }
        });

        add(createBtnPnl(), new GridBagConstraints(0, 6, 1, 2, 1.0d, 1.0d, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
                new Insets(6, 0, 6, 6), 0, 0));
        setTitle("Add Deal");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        UIToolKit.centerComponent(this);
    }

    private void refreshCategoryBox(DealType type) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(new Vector<DealCategory>(categoryManager.getCategoryList(type)));
        categoryBox.setModel(model);
    }

    private JPanel createBtnPnl() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                String checkRet = check();
//                if (StringUtil.notNullOrEmptyString(checkRet)) {
//                    JOptionPane.showMessageDialog(DealDialog.this, checkRet);
//                    return;
//                }

                Account account = (Account) accountBox.getSelectedItem();
                DealType type = (DealType) dealTypeBox.getSelectedItem();
                BigDecimal amount = null;
                try {
                    amount = new BigDecimal(format.parseObject(amountFld.getText()).toString());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                deal = new Deal(new Date(((java.util.Date) dateSpinner.getValue()).getTime()),
                        account, type,
                        (DealCategory) categoryBox.getSelectedItem(), detailFld.getText(),
                        amount, -1, BalanceCaculator.calBalance(account.getAccountType(), account.getBalance(), type,
                        amount), null);
                accountManager.updateBalance(deal, EvtType.ADD);
                returnMode = DialogReturnMode.RETURN_OK;
                dispose();
            }
        });
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnMode = DialogReturnMode.RETURN_CANCEL;
                dispose();
            }
        });

        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);

        return btnPanel;
    }

    public DialogReturnMode getReturnMode() {
        return returnMode;
    }

    public Deal getDeal() {
        return deal;
    }
}
