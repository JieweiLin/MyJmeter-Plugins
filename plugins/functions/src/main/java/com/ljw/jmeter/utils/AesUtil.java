package com.ljw.jmeter.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 数据库字段AES加解密
 *
 * @author 林杰炜 linjw
 * @date 2019-05-31 19:09
 */
public class AesUtil {

    /**
     * 加密(AES加密后转16进制)
     *
     * @param plainText 明文
     * @param secretKey 密钥
     * @return
     */
    public static String encrypt(String plainText, String secretKey) {
        try {
            byte[] rawKey = Arrays.copyOf(secretKey.getBytes(StandardCharsets.US_ASCII), 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encodeHex(encypted)).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     *
     * @param cipherText 密文
     * @param secretKey  密钥
     * @return
     */
    public static String decrypt(String cipherText, String secretKey) {
        try {
            byte[] encrypted = Hex.decodeHex(cipherText.toLowerCase().toCharArray());
            byte[] rawKey = Arrays.copyOf(secretKey.getBytes(StandardCharsets.US_ASCII), 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted);
        } catch (Exception e) {
            return "";
        }
    }

}
