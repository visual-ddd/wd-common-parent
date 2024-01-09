package com.wakedata.common.redis.lock;

/**
 * @author hhf
 * @date 2021/12/21
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = RedisTestApplication.class)
//public class RedisLockTest {
//    @Autowired
//    TestService testService;
//    /**
//     * 多线程获取锁测试
//     *
//     * @throws Exception
//     */
//    @Test
//    public void multithreadingTest() throws Exception {
//        ExecutorService executorService = Executors.newFixedThreadPool(6);
//        IntStream.range(0, 3).forEach(i -> executorService.submit(() -> {
//            try {
//                System.out.println("线程:[" + Thread.currentThread().getName() + "]尝试获取锁=》" + LocalDateTime.now());
//                String result = testService.getValue("sleep");
//                System.out.println("线程:[" + Thread.currentThread().getName() + "]拿到结果=》" + result + LocalDateTime.now());
//            } catch (Exception e) {
//                System.err.println("线程:[" + Thread.currentThread().getName() + "]获取锁失败=》" + LocalDateTime.now());
//            }
//        }));
//        executorService.awaitTermination(15, TimeUnit.SECONDS);
//    }
//}
