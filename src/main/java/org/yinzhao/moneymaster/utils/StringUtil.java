package org.yinzhao.moneymaster.utils;

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
