package org.yinzhao.moneymaster.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.yinzhao.moneymaster.accounts.Account;
import org.yinzhao.moneymaster.category.DealCategory;
import org.yinzhao.moneymaster.deals.Deal;
import org.yinzhao.moneymaster.enums.AccountType;
import org.yinzhao.moneymaster.enums.DealType;

import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public final class DBUtil {

    private static SessionFactory sessionFactory;

    static {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    public static SessionFactory buildSessionFactory() {
        return sessionFactory;
    }

    public static void addAccount2DB(Account account) {
        if (account != null) {
            if (sessionFactory != null) {
                Session session = sessionFactory.openSession();
                session.beginTransaction();
                session.save(account);
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public static List<Account> queryAccount() {
        List<Account> accountList = new ArrayList<Account>();
        if (sessionFactory != null) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<Account> accounts = session.createQuery("from Account").list();
            if (accounts != null && !accounts.isEmpty()) {
                accountList.addAll(accounts);
            }
            session.getTransaction().commit();
            session.close();
        }
        return accountList;
    }

    public static List<DealCategory> queryCategory() {
        List<DealCategory> categoryList = new ArrayList<DealCategory>();
        if (sessionFactory != null) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<DealCategory> categories = session.createQuery("from DealCategory").list();
            if (categories != null && !categories.isEmpty()) {
                categoryList.addAll(categories);
            }
            session.getTransaction().commit();
            session.close();
        }
        return categoryList;
    }

    public static void addCategory2DB(DealCategory category) {
        if (category != null) {
            if (sessionFactory != null) {
                Session session = sessionFactory.openSession();
                session.beginTransaction();
                session.save(category);
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public static List<Deal> queryDeals() {
        List<Deal> dealList = new ArrayList<Deal>();
        if (sessionFactory != null) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<Deal> deals = session.createQuery("from Deal").list();
            if (deals != null && !deals.isEmpty()) {
                dealList.addAll(deals);
            }
            session.getTransaction().commit();
            session.close();
        }
        return dealList;
    }

    public static void addDeal2DB(Deal deal) {
        if (deal != null && sessionFactory != null) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(deal);
            session.getTransaction().commit();
            session.close();
        }
    }

    public static void closeDBConnection() {
        sessionFactory.close();
    }

    public static void delDeal(Deal deal) {
        if (deal != null && sessionFactory != null) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(deal);
            session.getTransaction().commit();
            session.close();
        }
    }

    public static void updateAccount(Account account) {
        if (account != null && sessionFactory != null) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(account);
            session.getTransaction().commit();
            session.close();
        }
    }

    public static void updateDeal(Deal deal) {
        if (deal != null && sessionFactory != null) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(deal);
            session.getTransaction().commit();
            session.close();
        }
    }
}
