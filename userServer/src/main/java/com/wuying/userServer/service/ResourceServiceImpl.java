package com.wuying.userServer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuying.common.pojo.ResourceInfo;
import com.wuying.common.pojo.User;
import com.wuying.common.util.Util;
import com.wuying.userServer.mapper.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.FileInfo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
//@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ResourceServiceImpl implements ResourceService {
    private final UserMapper userMapper;
    private final HttpServletResponse response;
    private final RedissonClient redissonClient;
    private final Environment env;
    @Override
    public List<ResourceInfo> getAvailable(Principal principal) {
        Path dir = Paths.get("/data/files".concat(principal.getName()));

        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            throw new IllegalArgumentException("文件夹不存在：" + "/data/files".concat(principal.getName()));
        }

        List<ResourceInfo> files = new ArrayList<>();
        try (Stream<Path> stream = Files.list(dir)) {
            files = stream
                    .filter(Files::isRegularFile)
                    .map(path -> {
                            return ResourceInfo.builder().build();
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }
}
