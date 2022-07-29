package com.servletdemo.utils;

public class Tools {
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isnEmpty(String str) {
        return !isEmpty(str);
    }
}
