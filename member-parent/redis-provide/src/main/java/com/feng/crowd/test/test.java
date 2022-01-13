package com.feng.crowd.test;


import com.feng.crowd.controller.redisController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class test {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private com.feng.crowd.controller.redisController redisController;

    @Test
    public void test(){
        ValueOperations<String,String> operations=redisTemplate.opsForValue();
        operations.set("feng","guo");
    }


}
