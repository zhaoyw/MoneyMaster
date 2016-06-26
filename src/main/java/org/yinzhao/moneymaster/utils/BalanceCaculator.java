package org.yinzhao.moneymaster.utils;

import org.yinzhao.moneymaster.deals.Deal;
import org.yinzhao.moneymaster.enums.AccountType;
import org.yinzhao.moneymaster.enums.DealType;
import org.yinzhao.moneymaster.enums.EvtType;

import java.math.BigDecimal;

public class BalanceCaculator {
    public static BigDecimal calBalance(AccountType curAccountType, BigDecimal curBalance, Deal deal, EvtType type) {
        if (curBalance != null && deal != null && type != null && curAccountType != null) {
            if (curAccountType == AccountType.ASSETS) {
                if (type == EvtType.ADD) {
                    if (deal.getType() == DealType.OUT) {
                        return curBalance.subtract(deal.getAmount());
                    } else {
                        return curBalance.add(deal.getAmount());
                    }
                } else if (type == EvtType.DEL) {
                    if (deal.getType() == DealType.OUT) {
                        return curBalance.add(deal.getAmount());
                    } else {
                        return curBalance.subtract(deal.getAmount());
                    }
                }
            } else {
                if (type == EvtType.ADD) {
                    if (deal.getType() == DealType.OUT) {
                        return curBalance.add(deal.getAmount());
                    } else {
                        return curBalance.subtract(deal.getAmount());
                    }
                } else if (type == EvtType.DEL) {
                    if (deal.getType() == DealType.OUT) {
                        return curBalance.subtract(deal.getAmount());
                    } else {
                        return curBalance.add(deal.getAmount());
                    }
                }
            }
        }
        return new BigDecimal("0");
    }

    public static BigDecimal calBalance(AccountType accountType, BigDecimal curBalance, DealType type, BigDecimal amount) {
        if (accountType == AccountType.ASSETS) {
            if (type == DealType.IN) {
                return curBalance.add(amount);
            } else {
                return curBalance.subtract(amount);
            }
        } else {
            if (type == DealType.IN) {
                return curBalance.subtract(amount);
            } else {
                return curBalance.add(amount);
            }
        }
    }
}
