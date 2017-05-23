package com.duxl.mobileframe.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {
    private static MessageDigest messageDigest;
    private static final String TAG = "MD5Utils";

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    /**
     * 对字符串进行加密
     *
     * @param source 需要加密的字符串
     * @return 加密后的字符串（小写）
     */
    public static String messageDigest(String source) {
        if (source == null) {
            return null;
        }

        byte[] strTemp = source.getBytes();
        messageDigest.update(strTemp);
        byte[] md = messageDigest.digest();
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * 对字符串进行加密
     *
     * @param md5Str 需要加密的字符串
     * @return 加密后的字符串（大写）
     */
    public static String getMD5(String md5Str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(md5Str.getBytes("utf-8"));
            StringBuffer sb = new StringBuffer();
            String temp = "";
            for (byte b : bytes) {
                temp = Integer.toHexString(b & 0XFF);
                sb.append(temp.length() == 1 ? "0" + temp : temp);
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 十六进制的ASCII表示，消息加密后由这些字符组成。
     */
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static void main(String args[]) {
        System.out.println(MD5Utils.getMD5("1"));
        System.out.println(MD5Utils.messageDigest("1"));
    }
}
