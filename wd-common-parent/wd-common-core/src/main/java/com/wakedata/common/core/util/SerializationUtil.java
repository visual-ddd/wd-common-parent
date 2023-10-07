package com.wakedata.common.core.util;

import java.io.*;

/**
 * 序列化工具
 */
public class SerializationUtil {

    public SerializationUtil() {
    }

    public static byte[] serialize(Object o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        byte[] buf = baos.toByteArray();
        oos.flush();
        return buf;
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object obj = ois.readObject();
        return obj;
    }
}