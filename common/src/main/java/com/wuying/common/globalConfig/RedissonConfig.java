package com.wuying.common.globalConfig;

import com.wuying.common.pojo.ChatMessage;
import com.wuying.common.pojo.UserInfo;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setTimeout(3000)
                .setConnectionPoolSize(64)
                .setConnectionMinimumIdleSize(10);

        // 指定序列化类
        config.setCodec(new TypedJsonJacksonCodec(ChatMessage.class));
        config.setCodec(new TypedJsonJacksonCodec(UserInfo.class));

        return Redisson.create(config);
    }
}