package com.example.myapplication.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    //此处导入你的包名


        public static String md5Password(String password){
            StringBuffer sb = new StringBuffer();
            // 得到一个信息摘要器
            try {
                MessageDigest digest = MessageDigest.getInstance("md5");
                byte[] result = digest.digest(password.getBytes());
                // 把每一个byte做一个与运算 0xff
                for (byte b : result) {
                    // 与运算
                    int number = b & 0xff;
                    String str = Integer.toHexString(number);
                    if (str.length() == 1) {
                        sb.append("0");
                    }
                    sb.append(str);
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

}


