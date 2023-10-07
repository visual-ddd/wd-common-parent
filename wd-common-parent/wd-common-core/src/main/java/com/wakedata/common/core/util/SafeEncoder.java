package com.wakedata.common.core.util;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 从meizu common-redis迁移
 * @author hhf
 */
public class SafeEncoder {
    public SafeEncoder() {
    }

    public static byte[][] encodeMany(String... strs) {
        if (strs == null) {
            return (byte[][])null;
        } else {
            byte[][] many = new byte[strs.length][];

            for(int i = 0; i < strs.length; ++i) {
                many[i] = encode(strs[i]);
            }

            return many;
        }
    }

    public static String[] encodeMany(byte[]... data) {
        if (data == null) {
            return null;
        } else {
            String[] many = new String[data.length];

            for(int i = 0; i < data.length; ++i) {
                many[i] = encode(data[i]);
            }

            return many;
        }
    }

    public static Map<byte[], byte[]> encodeMap(Map<String, String> map) {
        Map<byte[], byte[]> byteMap = new LinkedHashMap();
        if (map != null) {
            Iterator entryIt = map.entrySet().iterator();

            while(entryIt.hasNext()) {
                Entry<String, String> e = (Entry)entryIt.next();
                byteMap.put(encode((String)e.getKey()), encode((String)e.getValue()));
            }
        }

        return byteMap;
    }

    public static Map<byte[], Double> encodeMapWithDouble(Map<String, Double> map) {
        Map<byte[], Double> byteMap = new LinkedHashMap();
        if (map != null) {
            Iterator entryIt = map.entrySet().iterator();

            while(entryIt.hasNext()) {
                Entry<String, Double> e = (Entry)entryIt.next();
                byteMap.put(encode((String)e.getKey()), e.getValue());
            }
        }

        return byteMap;
    }

    public static Map<String, String> encodeByteMap(Map<byte[], byte[]> bytemap) {
        Map<String, String> stringMap = new LinkedHashMap();
        if (bytemap != null) {
            Iterator entryIt = bytemap.entrySet().iterator();

            while(entryIt.hasNext()) {
                Entry<byte[], byte[]> e = (Entry)entryIt.next();
                stringMap.put(encode((byte[])e.getKey()), encode((byte[])e.getValue()));
            }
        }

        return stringMap;
    }

    public static List<String> encodeList(List<byte[]> bytelist) {
        if (bytelist == null) {
            return null;
        } else {
            List<String> ret = new ArrayList(bytelist.size());
            Iterator i$ = bytelist.iterator();

            while(i$.hasNext()) {
                byte[] b = (byte[])i$.next();
                ret.add(encode(b));
            }

            return ret;
        }
    }

    public static List<byte[]> encodeListToByte(List<String> list) {
        if (list == null) {
            return null;
        } else {
            List<byte[]> ret = new ArrayList(list.size());
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
                String b = (String)i$.next();
                ret.add(encode(b));
            }

            return ret;
        }
    }

    public static Set<String> encodeSet(Set<byte[]> byteset) {
        Set<String> ret = new LinkedHashSet();
        if (byteset != null) {
            Iterator i$ = byteset.iterator();

            while(i$.hasNext()) {
                byte[] b = (byte[])i$.next();
                ret.add(encode(b));
            }
        }

        return ret;
    }

    public static byte[] encode(String str) {
        try {
            if (str == null) {
                throw new RuntimeException("value sent to redis cannot be null");
            } else {
                return str.getBytes("UTF-8");
            }
        } catch (UnsupportedEncodingException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String encode(byte[] data) {
        if (data == null) {
            return null;
        } else {
            try {
                return new String(data, "UTF-8");
            } catch (UnsupportedEncodingException var2) {
                throw new RuntimeException(var2);
            }
        }
    }

    public static byte[] encode(Integer data) {
        return encode(String.valueOf(data));
    }
}
