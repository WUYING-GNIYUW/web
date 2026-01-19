package com.wuying.userServer.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuying.common.pojo.ResourceInfo;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import com.wuying.common.util.Util;
import com.wuying.userServer.mapper.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.FileInfo;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
//@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ResourceServiceImpl implements ResourceService {
    private final Environment env;
    @Override
    public List<ResourceInfo> getAvailable(Principal principal) {
        String fileRestoredPath = env.getProperty("file-restored-path");
        Path dir = null;
        if (fileRestoredPath != null) {
            dir = Paths.get(fileRestoredPath,principal.getName());
            if (!Files.exists(dir) || !Files.isDirectory(dir)) {
                throw new IllegalArgumentException("文件夹不存在");
            }
            else{
                List<ResourceInfo> files = new ArrayList<>();
                try (Stream<Path> stream = Files.list(dir)) {
                    files = stream
                            .filter(Files::isRegularFile)
                            .map(path -> {
                                return ResourceInfo.builder().filename(path.getFileName().toString()).build();
                            })
                            .collect(Collectors.toList());
                } catch (IOException e) {
                    log.atError().log(e.getMessage());
                }
                return files;
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<Result<?>> download(String resourceName,
                                              Principal principal) throws MalformedURLException {
        String fileRestoredPath = env.getProperty("file-restored-path");
        Path filePath = null;
        if (fileRestoredPath != null) {
            filePath = Paths.get(fileRestoredPath,principal.getName(),resourceName).resolve(resourceName).normalize();
            Resource resource;
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                return ResponseEntity.badRequest().body(Result.builder().message("resource unfounded").build());
            }
        }
        String baseInternalUri = "/internal_protected";
        String internalUri = baseInternalUri + Util.encodePath(principal.getName(), resourceName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Accel-Redirect", internalUri);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + resourceName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        log.atInfo().log("Generated internal URI:" + internalUri);

        return ResponseEntity.ok().headers(headers).body(Result.builder().message("resource downloaded").build());
    }

    @Override
    public ResponseEntity<Result<?>> upload(String resourceName,
                                            MultipartFile file,
                                            Principal principal) throws IOException {
        String fileRestoredPath = env.getProperty("file-restored-path");
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Result.builder().message("uploaded file is empty,unstored").build());
        }

        String originalFilename = file.getOriginalFilename();

        Path resourceStorePath = null;
        if (fileRestoredPath != null) {
            resourceStorePath = Paths.get(fileRestoredPath,principal.getName());
            if (!Files.exists(resourceStorePath)) {
                Files.createDirectories(resourceStorePath);
            }
            Path targetPath = null;
            if (originalFilename != null) {
                targetPath = resourceStorePath.resolve(originalFilename);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return ResponseEntity.ok(Result.builder().message("resource stored").build());

    }



}
