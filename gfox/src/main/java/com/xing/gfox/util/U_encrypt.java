package com.xing.gfox.util;

import android.text.TextUtils;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密
 */
public class U_encrypt {
    /**
     * 16位MD5算法
     */
    public static String Md5_16(String plainText) {
        StringBuffer buf = null;
        if ("".equals(plainText)) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();

            int i;

            buf = new StringBuffer();
            for (byte value : b) {
                i = value;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (buf != null) {
            return (buf.toString().substring(8, 24)).toUpperCase();
        } else {
            return "";
        }
    }

    /**
     * 32位MD5算法
     */
    public static String Md5_32(String source, String salt, boolean toUpperCase) {
        source = source + salt;
        StringBuilder sb = new StringBuilder(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(source.getBytes(StandardCharsets.UTF_8));

            for (byte b : array) {
                if (toUpperCase) {
                    sb.append(Integer.toHexString((b & 0xFF) | 0x100).toUpperCase().substring(1, 3));
                } else {
                    sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * DES加密
     *
     * @param encryptString 明文
     * @param keyStr        解密key
     * @param ivStr         偏移量
     * @return 密文
     */
    public static String DESEncrypt(String encryptString, String keyStr, String ivStr) throws Exception {
        if (TextUtils.isEmpty(encryptString) || "null".equals(encryptString) || "[]".equals(encryptString))
            return "";
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
        DESKeySpec dks = new DESKeySpec(keyStr.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return new String(Base64.encode(cipher.doFinal(encryptString.getBytes()), Base64.NO_WRAP));
    }

    /**
     * DES解密
     *
     * @param decryptString 密文
     * @param keyStr        加密key
     * @param ivStr         加密偏移量
     * @return 明文
     */
    public static String DESDecrypt(String decryptString, String keyStr, String ivStr) throws Exception {
        if (TextUtils.isEmpty(decryptString) || "null".equals(decryptString) || "[]".equals(decryptString))
            return "";
        IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
        DESKeySpec dks = new DESKeySpec(keyStr.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return new String(cipher.doFinal(Base64.decode(decryptString, Base64.NO_WRAP)));
    }

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     */
    public static String DES3Encrypt(String plainText, String secretKey, String offset) throws Exception {
        Key deskey;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(offset.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(encryptData, Base64.NO_WRAP);
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密数据
     * @param secretKey   秘钥
     * @param offset      偏移量
     * @return 解密数据
     */
    public static String DES3Decrypt(String encryptText, String secretKey, String offset) throws Exception {
        if (TextUtils.isEmpty(encryptText) || "null".equals(encryptText) || "[]".equals(encryptText))
            return "";
        Key deskey;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(offset.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText, Base64.NO_WRAP));
        return new String(decryptData, StandardCharsets.UTF_8);
    }

    /**
     * AES加密
     *
     * @param data      未加密数据
     * @param secretKey 秘钥
     * @param offset    偏移量
     * @return 加密数据
     */
    public static String AESEncrypt(String data, String secretKey, String offset) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.US_ASCII), "AES");
            //使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            //此处使用BASE64做转码
            return Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param encryptText 加密数据
     * @param secretKey   秘钥
     * @param offset      偏移量
     * @return 解密数据
     */
    public static String AESDecrypt(String encryptText, String secretKey, String offset) throws Exception {
        if (TextUtils.isEmpty(encryptText) || "null".equals(encryptText) || "[]".equals(encryptText))
            return "";
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.US_ASCII), "AES");
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(offset.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] buffer = Base64.decode(encryptText, Base64.NO_WRAP);
        byte[] encrypted = cipher.doFinal(buffer);
        //此处使用BASE64做转码
        return new String(encrypted, StandardCharsets.UTF_8);
    }

    /**
     * 字符串 SHA512 加密
     *
     * @param strText 加密内容
     * @param strType SHA-512
     * @return
     */
    private static String SHA512Encrypt(final String strText, final String strType) {
        // 返回值
        String strResult = null;
        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte[] byteBuffer = messageDigest.digest();
                // 將 byte 轉換爲 string
                StringBuilder strHexString = new StringBuilder();
                // 遍歷 byte buffer
                for (byte b : byteBuffer) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
}
