package com.wakedata.common.core.hashids;

import cn.hutool.core.lang.Assert;
import com.wakedata.common.core.exception.BizException;
import com.wakedata.common.core.hashids.HashidsUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author luomeng
 * @Description id和字符串转换工具测试
 * @createTime 2022-05-12 21:51:00
 */
public class HashidsUtilTest {

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args){
//
//        long length = 1000000000L;
        long length = 1000L;
        String salt = "ewaq5325f4tgqt430uagjtqp";
        int minLength = 8;
        Set<String> hashSet = new HashSet<>();
//        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
//                .setNamePrefix("pool-test").build();
//        ExecutorService poolExecutor = new ThreadPoolExecutor(20, 100,
//                5L, TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        for (long i = 0; i < length; i++) {
//            String hash = HashidsUtil.encodeDefault(i);
//            hashSet.add(hash);
//            System.out.println(Thread.currentThread().getName() + "-HashidsUtil.encodeDefault(" + i + ") ：" + hash);
//            System.out.println(Thread.currentThread().getName() + "-HashidsUtil.decodeDefault(" + hash + ") ： " + HashidsUtil.decodeDefault(hash));
//            String hash2 = HashidsUtil.encode(i,salt,minLength);
//            hashSet.add(hash2);
//            System.out.println(Thread.currentThread().getName() + "-HashidsUtil.encode(" + i + ") ：" + hash2);
//            System.out.println(Thread.currentThread().getName() + "-HashidsUtil.decode(" + hash2 + ") ： " + HashidsUtil.decode(hash2,salt,minLength));

            String hash4 = HashidsUtil.encodeHex("9007199254740992"+i,null);
            System.out.println("HashidsUtil.encodeHex(" + i + ") ： " + hash4);
            System.out.println("HashidsUtil.decodeHex("+hash4+") = " + HashidsUtil.decodeHex(hash4, null));

            String hash5 = HashidsUtil.encodeHex("abca"+i,salt);
            System.out.println("HashidsUtil.encodeHex(" + i + ",salt) ： " + hash5);
            System.out.println("HashidsUtil.decodeHex("+hash5+",salt) = " + HashidsUtil.decodeHex(hash5, salt));

        }

//        String yDZnAj8M80cGWO7vY = HashidsUtil.decodeHex("yDZnAj8M80cGWO7vY", salt);
//        Assert.isTrue(hashSet.size() == length*2);
//
//        System.out.println("HashidsUtil.encodeDefault(13075611505) = " + HashidsUtil.encodeDefault(13075611505L));
//
//        try {
//            System.out.println("HashidsUtil.decodeDefault(\"abcdefg1234\") = " + HashidsUtil.decodeDefault("abcdefg1234567"));
//        }catch (BizException e){
//            e.printStackTrace();
//        }
    }


}
