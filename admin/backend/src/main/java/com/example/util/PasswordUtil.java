package com.example.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 密码工具类
 */
public class PasswordUtil {

    private static final String DIGITS = "0123456789";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALL_CHARS = DIGITS + LOWERCASE + UPPERCASE;

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成随机密码
     * - 长度：8-12 位
     * - 至少包含数字和字母两种
     *
     * @return 随机密码明文
     */
    public static String generateRandomPassword() {
        // 长度范围 8-12
        int length = 8 + RANDOM.nextInt(5);

        // 确保至少包含 1 个数字和 1 个字母
        List<Character> chars = new ArrayList<>();
        chars.add(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        chars.add(LOWERCASE.charAt(RANDOM.nextInt(LOWERCASE.length())));

        // 剩余位置随机填充
        for (int i = 2; i < length; i++) {
            chars.add(ALL_CHARS.charAt(RANDOM.nextInt(ALL_CHARS.length())));
        }

        // 打乱顺序
        Collections.shuffle(chars, RANDOM);

        // 拼接成字符串
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }
}
