package com.pass.service.common.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 对称加解密算法
 * 
 * 
 * @author liangcm
 * @date 2017年11月7日
 * @since 1.0
 */
public class SecurityUtil {
    private final static Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    /**
     * optional value AES/DES/DESede
     */
    public static final String DES = "AES";
    /**
     * optional value AES/DES/DESede
     */
    public static final String CIPHER_ALGORITHM = "AES";


    private static Key getKey(String strKey) {
        try {
            if (strKey == null) {
                strKey = "";
            }
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            generator.init(128, secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(" 初始化密钥出现异常 ");
        }
    }

    /**
     * 加密 1.构造密钥生成器 2.根据key规则初始化密钥生成器 3.产生密钥 4.创建和初始化密码器 5.内容加密 6.返回字符串
     * 
     * 
     * @param data
     * @param key
     * @return
     */
    public static String encrypt(String data, String key) {
        try {
            SecureRandom sr = new SecureRandom();
            Key secureKey = getKey(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);
            byte[] bt = cipher.doFinal(data.getBytes());
            String strS = new BASE64Encoder().encode(bt);
            return strS;
        } catch (NoSuchAlgorithmException e) {
            logger.error("encrypt {} error", data, e);
        } catch (NoSuchPaddingException e) {
            logger.error("encrypt {} error", data, e);
        } catch (InvalidKeyException e) {
            logger.error("encrypt {} error", data, e);
        } catch (IllegalBlockSizeException e) {
            logger.error("encrypt {} error", data, e);
        } catch (BadPaddingException e) {
            logger.error("encrypt {} error", data, e);
        }
        return null;
    }


    /**
     * 解密 解密过程： 1.同加密1-4步 2.将加密后的字符串反纺成byte[]数组 3.将加密内容解密
     * 
     * 
     * @param message
     * @param key
     * @return
     */
    public static String decrypt(String message, String key) {
        try {
            SecureRandom sr = new SecureRandom();
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            Key secureKey = getKey(key);
            cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);
            byte[] res;
            res = new BASE64Decoder().decodeBuffer(message);
            res = cipher.doFinal(res);
            return new String(res);
        } catch (NoSuchAlgorithmException e) {
            logger.error("decrypt {} error", message, e);
        } catch (NoSuchPaddingException e) {
            logger.error("decrypt {} error", message, e);
        } catch (InvalidKeyException e) {
            logger.error("decrypt {} error", message, e);
        } catch (IllegalBlockSizeException e) {
            logger.error("decrypt {} error", message, e);
        } catch (BadPaddingException e) {
            logger.error("decrypt {} error", message, e);
        } catch (IOException e) {
            logger.error("decrypt {} error", message, e);
        }
        return null;
    }

}
