package com.zx.fss.util;

import org.apache.commons.lang3.text.StrBuilder;

import java.io.File;

public class StringUtil {
    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;
    private static final int PAD_LIMIT = 8192;

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str) || "null".equals(str);
    }

    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    public static String uncapitalize(String str) {
        int strLen;
        return str != null && (strLen = str.length()) != 0 ? (new StrBuilder(strLen)).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString() : str;
    }

    public static String getConverPath(String path) {
        return path.replaceAll("\\.", "\\\\") + File.separator;

    }
}
