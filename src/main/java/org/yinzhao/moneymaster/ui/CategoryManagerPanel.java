package org.yinzhao.moneymaster.ui;

import org.yinzhao.moneymaster.category.CategoryManager;
import org.yinzhao.moneymaster.category.DealCategory;
import org.yinzhao.moneymaster.category.ICategoryChangeListener;
import org.yinzhao.moneymaster.enums.DialogReturnMode;
import org.yinzhao.moneymaster.enums.EvtType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by zhaoyongwang on 16/5/29.
 */
public class CategoryManagerPanel extends JPanel implements ICategoryChangeListener {
    private CategoryManager categoryManager;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JButton addBtn;
    private JButton delBtn;

    long seq = 1;

    public CategoryManagerPanel(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
        this.categoryManager.clearListeners();
        this.categoryManager.addListener(this);
        initUI();
        loadData();
    }

    private void initUI() {
        String[] colNames = new String[]{"SequenceID", "Name", "Type"};
        tableModel = new DefaultTableModel(colNames, 0);
        categoryTable = new JTable(tableModel);

        setLayout(new GridBagLayout());
        add(new JScrollPane(categoryTable), new GridBagConstraints(0, 0, 1, 1, 1.0d, 1.0d, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));

        JPanel btnPanel = createBtnPanel();
        add(btnPanel, new GridBagConstraints(0, 1, 1, 1, 0.0d, 0.0d, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets(6, 6, 6, 6), 0, 0));
    }

    private JPanel createBtnPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));

        addBtn = new JButton("Add Category");
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TCategoryDialog dialog = new TCategoryDialog(null);
                dialog.setVisible(true);
                if (dialog.getReturnMode() == DialogReturnMode.RETURN_OK) {
                    DealCategory category = dialog.getCategory();
                    categoryManager.addCategory(category);
                }
            }
        });
        delBtn = new JButton("Delete Category");
        delBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedIndices = categoryTable.getSelectedRows();
                if (selectedIndices.length > 0) {
                    //TODO
                }
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(delBtn);

        return btnPanel;
    }

    public void categoryChanged(EvtType type, DealCategory category) {
        if (type != null && category != null) {
            if (type == EvtType.ADD) {
                addCategory2Table(category);
            }
        }
    }

    private void addCategory2Table(DealCategory category) {
        Object[] rowData = new Object[]{seq++, category.getName(), category.getType()};
        tableModel.addRow(rowData);
        categoryTable.revalidate();
    }

    private void loadData() {
        java.util.List<DealCategory> categoryList = categoryManager.getCategoryList();
        if (!categoryList.isEmpty()) {
            for (DealCategory category : categoryList) {
                addCategory2Table(category);
            }
        }
    }
}
