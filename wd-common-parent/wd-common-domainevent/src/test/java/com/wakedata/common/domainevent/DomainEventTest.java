package com.wakedata.common.domainevent;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {SpringBootStart.class})
@RunWith(SpringRunner.class)
public class DomainEventTest {

    @Test
    public void domainEventPublisherTest() {
        // 发送领域事件
        DomainEventPublisher.getInstance().postSync(new DomainEventA());
        DomainEventPublisher.getInstance().postSync(new DomainEventB());
        Assert.assertTrue(true);
    }

}

