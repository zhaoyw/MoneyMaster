package org.yinzhao.moneymaster.ui;

import org.yinzhao.moneymaster.category.DealCategory;
import org.yinzhao.moneymaster.enums.DealType;
import org.yinzhao.moneymaster.enums.DialogReturnMode;
import org.yinzhao.moneymaster.utils.StringUtil;
import org.yinzhao.moneymaster.utils.UIToolKit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by zhaoyongwang on 16/5/29.
 */
public class TCategoryDialog extends JDialog {
    public TCategoryDialog(Window parent) {
        super(parent);
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());

        add(new JLabel("Category Type"), new GridBagConstraints(0, 0, 1, 1, 0.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        typeBox = new JComboBox(new DefaultComboBoxModel(DealType.values()));
        add(typeBox, new GridBagConstraints(1, 0, 1, 1, 1.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 0, 6, 6), 0, 0));
        add(new JLabel("Category Name"), new GridBagConstraints(0, 1, 1, 1, 0.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        nameFld = new JTextField();
        nameFld.setPreferredSize(new Dimension(100, 25));
        add(nameFld, new GridBagConstraints(1, 1, 1, 1, 1.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(6, 0, 6, 6), 0, 0));


        JPanel btnPanel = createBtnPnl();
        add(btnPanel, new GridBagConstraints(1, 2, 1, 1, 0.0d, 0.0d, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(6, 6, 6, 0), 0, 0));

        setTitle("Add Category");
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
                    JOptionPane.showMessageDialog(TCategoryDialog.this, checkRet);
                    return;
                }

                category = new DealCategory((DealType) typeBox.getSelectedItem(), nameFld.getText());
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
        String categoryName = nameFld.getText();
        if (StringUtil.nullOrEmptyString(categoryName)) {
            return "Category name can not be null!";
        }
        return null;
    }

    public DialogReturnMode getReturnMode() {
        return returnMode;
    }

    public DealCategory getCategory() {
        return category;
    }

    private JComboBox typeBox;
    private JTextField nameFld;

    private DialogReturnMode returnMode = DialogReturnMode.RETURN_CANCEL;
    private DealCategory category;
}
