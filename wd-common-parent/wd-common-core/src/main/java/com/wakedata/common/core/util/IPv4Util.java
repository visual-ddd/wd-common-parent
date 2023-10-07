package com.wakedata.common.core.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangtielong on 2018/11/9.
 */
public class IPv4Util {

    private static final String HEADER_FORWARDED_FOR = "x-forwarded-for";
    private static final String HEADER_PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String HEADER_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HEADER_HTTP_CLIENT_IP = "http_client_ip";
    private static final String HEADER_HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

    private static final String UNKNOWN = "unknown";
    private static final String CHAR_COLON = ":";
    private static final String CHAR_COMMA = ",";

    public static String getClientIp(HttpServletRequest request){
        String ip = request.getHeader(HEADER_FORWARDED_FOR);
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HEADER_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HEADER_WL_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HEADER_HTTP_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HEADER_HTTP_X_FORWARDED_FOR);
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.contains(CHAR_COMMA)) {
            ip = ip.substring(ip.lastIndexOf(CHAR_COMMA) + 1, ip.length()).trim();
        }
        //判断IP是否存在带有端口号的情况、应该要去掉端口号
        if (ip != null && ip.contains(CHAR_COLON)) {
            ip = ip.substring(0, ip.indexOf(CHAR_COLON));
        }
        return ip;
    }
}
