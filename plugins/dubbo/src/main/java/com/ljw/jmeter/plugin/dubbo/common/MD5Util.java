package com.ljw.jmeter.plugin.dubbo.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author linjw
 * @date 2019/12/19 17:43
 */
public class MD5Util {
    private static MessageDigest md;
    private static final char[] HEX_CODE = "0123456789ABCDEF".toCharArray();

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String MD516Bit(String input) {
        String hash = MD532Bit(input);
        if (hash == null) {
            return null;
        }
        return hash.substring(8, 24);
    }

    public static String MD532Bit(String input) {
        if (input == null || input.length() == 0) {
            return null;
        }
        md.update(input.getBytes());
        byte[] digest = md.digest();
        return convertToString(digest);
    }

    private static String convertToString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(HEX_CODE[(b >> 4) & 0xF]);
            r.append(HEX_CODE[(b & 0xF)]);
        }
        return r.toString();
    }
}
