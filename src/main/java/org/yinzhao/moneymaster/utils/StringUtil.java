package org.yinzhao.moneymaster.utils;

/**
 * Created by zhaoyongwang on 16/5/28.
 */
public final class StringUtil {
    private StringUtil() {
    }

    public static boolean nullOrEmptyString(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean notNullOrEmptyString(String str) {
        return !nullOrEmptyString(str);
    }
}
