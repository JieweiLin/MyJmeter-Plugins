package com.ljw.jmeter.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

/**
 * SM4加密解密工具类
 *
 * @author 林杰炜 linjw
 * @date 2019-06-11 19:45
 */
public class Sm4Util {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static final String ALGORITHM_NAME = "SM4";
    /**
     * 加密算法/分组加密模式/分组填充方式
     * PKCS5Padding-以8个字节为一组进行分组加密
     * 定义分组加密模式使用：PKCS5Padding
     */
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    /**
     * 128-32位16进制；256-64位16进制
     */
    public static final int DEFAULT_KEY_SIZE = 128;

    /**
     * 生成ECB暗号
     * ECB模式（电子密码本模式：Electronic codebook）
     *
     * @param algorithmName 算法名称
     * @param mode          模式
     * @param key           密钥
     * @return ECB暗号
     * @throws Exception 异常
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    /**
     * 自动生成密钥
     *
     * @return 密钥字节
     * @throws Exception 异常
     */
    public static byte[] generateKey() throws Exception {
        return generateKey(DEFAULT_KEY_SIZE);
    }

    /**
     * @param keySize 密钥长度
     * @return 密钥字节
     * @throws Exception 异常
     */
    public static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * sm4加密
     * 加密模式：ECB
     * 密文长度不固定，会随着被加密字符串长度的变化而变化
     *
     * @param hexKey   16进制密钥（忽略大小写）
     * @param paramStr 待加密字符串
     * @return 返回16进制的加密字符串
     * @throws Exception 异常
     */
    public static String encryptEcb(String hexKey, String paramStr) throws Exception {
        String cipherText = "";
        // 16进制字符串-->byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // String-->byte[]
        byte[] srcData = paramStr.getBytes(StandardCharsets.UTF_8);
        // 加密后的数组
        byte[] cipherArray = encryptEcbPadding(keyData, srcData);
        // byte[]-->hexString
        cipherText = ByteUtils.toHexString(cipherArray);
        return cipherText;
    }

    /**
     * 加密模式之Ecb
     *
     * @param key  密钥
     * @param data 待加密字节
     * @return 加密后字节
     * @throws Exception 异常
     */
    public static byte[] encryptEcbPadding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * sm4解密
     * 解密模式：采用ECB
     *
     * @param hexKey     16进制密钥
     * @param cipherText 16进制的加密字符串（忽略大小写）
     * @return 解密后的字符串
     * @throws Exception 异常
     */
    public static String decryptEcb(String hexKey, String cipherText) throws Exception {
        // 用于接收解密后的字符串
        String decryptStr = "";
        // hexString-->byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // hexString-->byte[]
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        // 解密
        byte[] srcData = decryptEcbPadding(keyData, cipherData);
        // byte[]-->String
        decryptStr = new String(srcData, StandardCharsets.UTF_8);
        return decryptStr;
    }

    /**
     * 解密
     *
     * @param key
     * @param cipherText
     * @return
     * @throws Exception
     * @explain
     */
    public static byte[] decryptEcbPadding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    /**
     * 校验加密前后的字符串是否为同一数据
     *
     * @param hexKey     16进制密钥（忽略大小写）
     * @param cipherText 16进制加密后的字符串
     * @param paramStr   加密前的字符串
     * @return 是否为同一数据
     * @throws Exception
     * @explain
     */
    public static boolean verifyEcb(String hexKey, String cipherText, String paramStr) throws Exception {
        // 用于接收校验结果
        boolean flag = false;
        // hexString-->byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // 将16进制字符串转换成数组
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        // 解密
        byte[] decryptData = decryptEcbPadding(keyData, cipherData);
        // 将原字符串转换成byte[]
        byte[] srcData = paramStr.getBytes(StandardCharsets.UTF_8);
        // 判断2个数组是否一致
        flag = Arrays.equals(decryptData, srcData);
        return flag;
    }
}
