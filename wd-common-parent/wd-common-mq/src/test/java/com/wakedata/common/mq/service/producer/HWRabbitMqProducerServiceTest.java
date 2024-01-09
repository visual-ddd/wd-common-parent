package com.wakedata.common.mq.service.producer;

import com.wakedata.common.mq.MqConsumerHelper;
import com.wakedata.common.mq.MqProducerHelper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

@Ignore
@SpringBootTest(classes = {SpringBootStart.class})
@RunWith(SpringRunner.class)
public class HWRabbitMqProducerServiceTest {

    @Test
    public void testSend() {
        IntStream.range(0, 5).forEach(
            i -> MqProducerHelper.publish("rabbitMq信息" + i, null, "exchange_test", "tag1-test"));
    }

    @Test
    public void testConsumer() {
        MqConsumerHelper.subscribe(System.out::println, "exchange_test", "tag1-test");
    }

    @Test
    public void testConsumer2() {
        MqConsumerHelper.subscribe(System.out::println, "exchange_test", "tag2-test");
    }
}