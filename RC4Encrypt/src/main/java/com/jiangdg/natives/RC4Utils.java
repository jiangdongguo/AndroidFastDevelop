package com.jiangdg.natives;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/***
 *  使用RC4算法加密流数据
 *
 * @author Jiangdg
 * @since 2019-08-20 10:40
 * */
public class RC4Utils {

    static {
        System.loadLibrary("rc4util");
    }

    /** 加密，使用默认密钥
     *
     * @param plainText 字符串明文
     * @return 字符串密文
     * */
    public static String rc4EncryptText(String plainText) {
        if(TextUtils.isEmpty(plainText)) {
            return null;
        }
        byte[] plainData = plainText.getBytes();
        nativeRc4Encrypt(null, plainData, 0, plainData.length);
        return Base64.encodeToString(plainData, Base64.DEFAULT);
    }

    /**
     *  解密，使用默认密钥
     *
     * @param cipherText 字符串密文
     * @return 字符串明文
     * */
    public static String rc4DecryptText(String cipherText){
        if(TextUtils.isEmpty(cipherText)) {
            return null;
        }
        byte[] cipherData = Base64.decode(cipherText, Base64.DEFAULT);
        nativeRC4Decrypt(null, cipherData, 0, cipherData.length);
        return new String(cipherData);
    }

    /**
     *  加密，使用默认密钥
     *
     * @param plain 明文(字节数组)
     * @return 密文
     * */
    public static int rc4EncryptData(byte[] plain, int start, int len) {
        return nativeRc4Encrypt(null, plain, start , len);
    }

    /**
     *  解密，使用默认密钥
     *
     * @param cipher 密文(字节数组)
     * @return 明文
     * */
    public static int rc4DecryptData(byte[] cipher, int start, int len) {
        return nativeRC4Decrypt(null, cipher, start , len);
    }

    public native static int nativeRc4Encrypt(String key, byte[] plainText, int start, int len);

    public native static int nativeRC4Decrypt(String key, byte[] cipherText, int start, int len);
}
