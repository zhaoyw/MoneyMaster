package org.yinzhao.moneymaster.ui;

import org.yinzhao.moneymaster.accounts.Account;
import org.yinzhao.moneymaster.enums.AccountType;
import org.yinzhao.moneymaster.enums.DialogReturnMode;
import org.yinzhao.moneymaster.utils.StringUtil;
import org.yinzhao.moneymaster.utils.UIToolKit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

public class TAccountDialog extends JDialog {

    public static final int DEFAULT_TXTFLD_WIDTH = 100;
    public static final int DEFAULT_TXTFLD_HEIGHT = 25;

    private NumberFormat format;

    public TAccountDialog(Window parent) {
        super(parent);
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());

        add(new JLabel("Account Type"), new GridBagConstraints(0, 0, 1, 1, 0.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        typeBox = new JComboBox(new DefaultComboBoxModel(AccountType.values()));
        add(typeBox, new GridBagConstraints(1, 0, 1, 1, 1.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 0, 6, 6), 0, 0));
        add(new JLabel("Account Name"), new GridBagConstraints(0, 1, 1, 1, 0.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        nameFld = new JTextField();
        nameFld.setPreferredSize(new Dimension(DEFAULT_TXTFLD_WIDTH, DEFAULT_TXTFLD_HEIGHT));
        add(nameFld, new GridBagConstraints(1, 1, 1, 1, 1.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 0, 6, 6), 0, 0));

        add(new JLabel("Balance"), new GridBagConstraints(0, 2, 1, 1, 0.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        banlanceFld = new JFormattedTextField(format);
        banlanceFld.setPreferredSize(new Dimension(DEFAULT_TXTFLD_WIDTH, DEFAULT_TXTFLD_HEIGHT));
        add(banlanceFld, new GridBagConstraints(1, 2, 1, 1, 1.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 0, 6, 6), 0, 0));

        JPanel btnPanel = createBtnPnl();
        add(btnPanel, new GridBagConstraints(1, 3, 1, 1, 0.0d, 0.0d, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(6, 6, 6, 0), 0, 0));

        setTitle("Add Account");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        UIToolKit.centerComponent(this);
    }

    private JPanel createBtnPnl() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String checkRet = check();
                if (StringUtil.notNullOrEmptyString(checkRet)) {
                    JOptionPane.showMessageDialog(TAccountDialog.this, checkRet);
                    return;
                }

                account = new Account((AccountType) typeBox.getSelectedItem(), nameFld.getText(), getBalance());
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

    private String check() {
        String accountName = nameFld.getText();
        if (StringUtil.nullOrEmptyString(accountName)) {
            return "Account name can not be null!";
        }
        return null;
    }

    private BigDecimal getBalance() {
        BigDecimal balance = new BigDecimal("0.00");
        try {
            balance = new BigDecimal(format.parseObject(banlanceFld.getText()).toString());
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return balance;
    }

    public DialogReturnMode getReturnMode() {
        return returnMode;
    }

    public Account getAccount() {
        return account;
    }

    private JComboBox typeBox;
    private JTextField nameFld;
    private JTextField banlanceFld;

    private DialogReturnMode returnMode = DialogReturnMode.RETURN_CANCEL;
    private Account account;
}
