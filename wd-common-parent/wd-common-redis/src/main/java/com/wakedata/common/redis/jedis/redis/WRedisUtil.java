package com.wakedata.common.redis.jedis.redis;

/**
 * @author pengxu
 * @Date 2019/9/27.
 */
@Deprecated
public class WRedisUtil {

    private static final byte[] SPLITTER = SafeEncoder.encode(".");

    private static final String STR_SPLITTER = ".";

    public static byte[] concatKey(WRedisClientConfig clientConfig, byte[] key) {
        byte[] finallyKey = key;
        if (clientConfig != null) {
            if (clientConfig.getKeyPrefix() != null && clientConfig.getKeyPrefix().length() > 0) {
                byte[] tempPrefix = concat(SafeEncoder.encode(clientConfig.getKeyPrefix()), SPLITTER);
                finallyKey = concat(tempPrefix, finallyKey);
            }
            if (clientConfig.getKeySuffix() != null && clientConfig.getKeySuffix().length() > 0) {
                byte[] tempSuffix = concat(SPLITTER,SafeEncoder.encode(clientConfig.getKeySuffix()));
                finallyKey = concat(finallyKey, tempSuffix);
            }
        }
        return finallyKey;
    }

    public static byte[][] concatKeys(WRedisClientConfig clientConfig,byte[][] keys) {
        byte[][] finallyKeys = new byte[keys.length][];
        for (int i = 0; i < keys.length; i++) {
            byte[] finallyKey = concatKey(clientConfig,keys[i]);
            finallyKeys[i] = finallyKey;
        }
        return finallyKeys;
    }


    public static byte[] concat(byte[]... arrays) {
        // Determine the length of the result array
        int totalLength = 0;
        for (int i = 0; i < arrays.length; i++) {
            totalLength += arrays[i].length;
        }
        // create the result array
        byte[] result = new byte[totalLength];
        // copy the source arrays into the result array
        int currentIndex = 0;
        for (int i = 0; i < arrays.length; i++) {
            System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
            currentIndex += arrays[i].length;
        }
        return result;
    }

    public static String concatKey(WRedisClientConfig clientConfig, String key) {
        String finallyKey = key;
        if (clientConfig != null) {
            if (clientConfig.getKeyPrefix() != null && clientConfig.getKeyPrefix().length() > 0) {
                String tempPrefix = clientConfig.getKeyPrefix() + STR_SPLITTER;
                finallyKey = tempPrefix + finallyKey;
            }
            if (clientConfig.getKeySuffix() != null && clientConfig.getKeySuffix().length() > 0) {
                String tempSuffix = STR_SPLITTER + clientConfig.getKeySuffix();
                finallyKey = finallyKey + tempSuffix;
            }
        }
        return finallyKey;
    }

    public static String[] concatKeys(WRedisClientConfig clientConfig, String[] key) {
        String[] finallyKey = key;
        if(key.length == 0){
            return key;
        }
        for(int i=0; i<key.length; i++){
            String oneKey = key[i];
            finallyKey[i] = concatKey(clientConfig,oneKey);
        }
        return finallyKey;
    }

}
