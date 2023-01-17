package com.my9z.study;

import com.my9z.study.core.api.WczMQProducer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class MQTest {

    @Autowired
    WczMQProducer mqProducer;

    @Test
    public void testESConfigProperties() throws Exception {
        mqProducer.sendSync("TopicSync",null,"hello consumer","wczy");
    }


}
