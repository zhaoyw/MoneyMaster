package org.yinzhao.moneymaster.ui;

import org.yinzhao.moneymaster.accounts.AccountManager;
import org.yinzhao.moneymaster.category.CategoryManager;
import org.yinzhao.moneymaster.deals.DealManager;
import org.yinzhao.moneymaster.utils.DBUtil;
import org.yinzhao.moneymaster.utils.UIToolKit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public MainFrame() {
        initUI();
        setTitle("e账通");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(WIDTH, HEIGHT));
        UIToolKit.centerComponent(this);
    }

    private void initUI() {
        initMenuBar();
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        JMenuItem dealManagerItem = new JMenuItem("Deal Manager");
        dealManagerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openDealManager();
            }
        });
        fileMenu.add(dealManagerItem);
        menuBar.add(fileMenu);

        accountMenu = new JMenu("Account");
        menuBar.add(accountMenu);

        JMenuItem accountManagerItem = new JMenuItem("Account Manager");
        accountManagerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAccountManager();
            }
        });
        accountMenu.add(accountManagerItem);


        settingMenu = new JMenu("Setting");
        JMenuItem categoryManagerItem = new JMenuItem("Category Manager");
        categoryManagerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCategoryManager();
            }
        });
        settingMenu.add(categoryManagerItem);
        menuBar.add(settingMenu);

        setJMenuBar(menuBar);
    }

    private void openDealManager() {
        getContentPane().removeAll();
        getContentPane().add(new DealManagerPanel(dealManager, accountManager, categorManager));
        getContentPane().revalidate();
    }

    private void openAccountManager() {
        getContentPane().removeAll();
        getContentPane().add(new AccountManagerPanel(accountManager));
        getContentPane().revalidate();
    }

    private void openCategoryManager() {
        getContentPane().removeAll();
        getContentPane().add(new CategoryManagerPanel(categorManager));
        getContentPane().revalidate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getNewState() == WindowEvent.WINDOW_CLOSING) {
            DBUtil.closeDBConnection();
        }
        super.processWindowEvent(e);
    }

    private AccountManager accountManager = new AccountManager();
    private CategoryManager categorManager = new CategoryManager();
    private DealManager dealManager = new DealManager();

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu accountMenu;
    private JMenu settingMenu;
}
