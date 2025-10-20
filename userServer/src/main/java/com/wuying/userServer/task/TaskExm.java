package com.wuying.userServer.task;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskExm {

    private final SimpMessagingTemplate messagingTemplate;
    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> future;

//    public TaskExm() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.initialize();
//        this.taskScheduler = scheduler;
//    }

    // 任务内容
    private void task(String sessionId) {
        messagingTemplate.convertAndSendToUser(sessionId, "/queue/messages", "定时消息: " + System.currentTimeMillis());
        System.out.println("定时任务执行: " + System.currentTimeMillis());
    }

    // 后端接口调用启动任务
    public void startTask(String sessionId) {
        if (future == null || future.isCancelled()) {
            future = taskScheduler.scheduleAtFixedRate( () -> task(sessionId), Duration.ofSeconds(5));
            System.out.println("定时任务已启动");
        }
    }

    // 可选：停止任务
    public void stopTask() {
        if (future != null) {
            future.cancel(false);
            System.out.println("定时任务已停止");
        }
    }
}