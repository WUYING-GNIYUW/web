package com.wuying.userServer.listener;

import com.wuying.common.pojo.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StreamListenerConfig {

    private final Environment env;
    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, ChatMessage>> streamContainer(
            RedisConnectionFactory factory,
            ChatMessageListener chatMessageListener
    ) throws UnknownHostException {

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, ChatMessage>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .pollTimeout(Duration.ofSeconds(2))  // long polling
                        .targetType(ChatMessage.class)
                        .build();

        StreamMessageListenerContainer<String, ObjectRecord<String, ChatMessage>> container =
                StreamMessageListenerContainer.create(factory, options);


        container.receive(
                Consumer.from("chat-group", "consumer"),
                StreamOffset.create("chat-stream:".concat(env.getProperty("spring.cloud.nacos.discovery.ip")), ReadOffset.lastConsumed()),
                chatMessageListener
        );
        container.start();
        return container;
    }


//    @Bean
//    public StreamMessageListenerContainer<String, ObjectRecord<String, ChatMessage>> streamContainer() throws UnknownHostException {
//        var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
//                .pollTimeout(Duration.ofSeconds(2))
//                .targetType(ChatMessage.class)
//                .build();
//
//        var container = StreamMessageListenerContainer.create(factory, options);
//
//        String instanceId = InetAddress.getLocalHost().getHostAddress();
//        String stream = "chat-stream"; // or "chat-stream:" + instanceId if you use per-instance stream
//        String group = "chat-group-" + instanceId; // 每节点一个组（方案2），或统一 "chat-group"（单组竞争）
//
//        // 自动创建 group & MKSTREAM（如果不存在）
//        StreamMessageListenerContainer.StreamReadRequest<String> request = StreamMessageListenerContainer.StreamReadRequest.builder(StreamOffset.create(stream, ReadOffset.lastConsumed()))
//                .consumer(Consumer.from(group, instanceId))
//                .createGroup(true)        // 自动 XGROUP CREATE ... MKSTREAM
//                .autoAcknowledge(true)    // 容器在 listener 成功返回后自动 XACK
//                .build();
//
//        container.register(request, chatStreamListener);
//        container.start();
//        return container;
//    }

}