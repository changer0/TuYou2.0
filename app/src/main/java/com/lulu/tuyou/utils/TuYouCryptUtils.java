package com.lulu.tuyou.utils;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by lulu on 2016/12/20.
 */

public class TuYouCryptUtils {
    //AES 加密操作
    public static byte[] aesEncryptSimple(byte[] data, byte[] key) {
        byte[] ret = null;
        if (data != null && key != null) {
            if (data.length > 0 && key.length == 16) {
                // AES 128bit = 16bytes
                try {
                    Cipher cipher = Cipher.getInstance("AES");
                    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
                    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
                    ret = cipher.doFinal(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
    //AES 解密操作
    public static byte[] aesDecryptSimple(byte[] data, byte[] key) {
        byte[] ret = null;
        if (data != null && key != null) {
            if (data.length > 0 && key.length == 16) {
                // AES 128bit = 16bytes
                try {
                    Cipher cipher = Cipher.getInstance("AES");
                    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
                    cipher.init(Cipher.DECRYPT_MODE, keySpec);
                    ret = cipher.doFinal(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
}
