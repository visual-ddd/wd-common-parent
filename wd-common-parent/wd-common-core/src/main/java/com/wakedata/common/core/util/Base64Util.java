package com.wakedata.common.core.util;


import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64Util
 *
 * @author focus
 * @date 2020/2/12
 **/

public class Base64Util {

    public static String encode(String str){
        try {
            return Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String decode(String str){
        try {
            return new String(Base64.getDecoder().decode(str),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
