package com.feng.crowd.api;


import com.feng.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

@FeignClient("feng-crowd-redis")
public interface RedisRemoteService {


    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(@RequestParam("key") String key, @RequestParam("value") String value);

    @RequestMapping("/set/redis/key/value/remote/withtime")
    ResultEntity<String> setRedisKeyValueRemoteWithTime(@RequestParam("key") String key,
                                                        @RequestParam("value") String value,
                                                        @RequestParam("time") long time,
                                                        @RequestParam("timeUnit")TimeUnit timeUnit);
    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> getRedisStringVlaueByKey(@RequestParam("key") String key);

    @RequestMapping("/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key);
}
