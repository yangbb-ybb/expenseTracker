package com.example.service.impl;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 语义化版本号比较工具
 *
 * 支持：1.0.0 / 1.2.3 / 1.10.0（按数字段比较，非字符串）
 * 缺失段补 0：compare("1.0", "1.0.0") == 0
 * 解析失败时回退到字符串比较，避免抛异常
 */
final class VersionComparator {

    private VersionComparator() {}

    static int compare(String a, String b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        try {
            int[] va = parse(a);
            int[] vb = parse(b);
            int len = Math.max(va.length, vb.length);
            for (int i = 0; i < len; i++) {
                int ai = i < va.length ? va[i] : 0;
                int bi = i < vb.length ? vb[i] : 0;
                if (ai != bi) return Integer.compare(ai, bi);
            }
            return 0;
        } catch (Exception ignore) {
            return a.compareTo(b);
        }
    }

    private static int[] parse(String v) {
        return Arrays.stream(v.split("\\."))
                .mapToInt(s -> {
                    // 兼容 "1.0.0-rc1" 这种带后缀的，只取数字前缀
                    int end = 0;
                    while (end < s.length() && Character.isDigit(s.charAt(end))) end++;
                    String head = end == 0 ? "0" : s.substring(0, end);
                    return Integer.parseInt(head);
                })
                .toArray();
    }

    /**
     * 取排序时按 Comparator 自然顺序
     */
    static final Comparator<String> NATURAL = VersionComparator::compare;
}
